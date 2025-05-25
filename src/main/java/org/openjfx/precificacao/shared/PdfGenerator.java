package org.openjfx.precificacao.shared;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.openjfx.precificacao.dtos.DetalhamentoDTO;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

    public void gerarPDF(String destino,
                         Map<String, Map<String, List<DetalhamentoDTO>>> agrupados,
                         String nomeProjeto, String nomeCliente,
                         String totalServicos, String custosFixos, String custosVariaveis, String lucro,
                         String despesasComplementares, String desconto, String subtotalComDesconto,
                         String impostos, String valorFinal) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(destino));
            document.open();

            // Logo
            Image logo = Image.getInstance(getClass().getResource("/images/sindomar_logo.png").toExternalForm());
            logo.scaleToFit(250, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(new Paragraph(" "));

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Orçamento de Serviços", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Dados do Projeto e Cliente
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(10f);
            addItemToTable(infoTable, "Projeto:", nomeProjeto, BaseColor.LIGHT_GRAY, BaseColor.BLACK);
            addItemToTable(infoTable, "Cliente:", nomeCliente, BaseColor.LIGHT_GRAY, BaseColor.BLACK);
            document.add(infoTable);

            // Tabela de etapas e atividades
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            for (String etapa : agrupados.keySet()) {
                PdfPCell etapaCell = new PdfPCell(new Phrase(etapa, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
                etapaCell.setColspan(2);
                etapaCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                etapaCell.setPadding(5);
                table.addCell(etapaCell);

                Map<String, List<DetalhamentoDTO>> atividades = agrupados.get(etapa);
                for (String atividade : atividades.keySet()) {
                    PdfPCell atv = new PdfPCell(new Phrase(atividade));
                    atv.setPadding(5);
                    table.addCell(atv);

                    float subtotal = atividades.get(atividade).stream()
                            .map(d -> d.getHoras() * d.getValorHoras())
                            .reduce(0f, Float::sum);
                    PdfPCell valor = new PdfPCell(new Phrase(FormatadorMoeda.formatarValorComoMoeda(subtotal)));
                    valor.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    valor.setPadding(5);
                    table.addCell(valor);
                }
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Totais finais
            PdfPTable totais = new PdfPTable(2);
            totais.setWidthPercentage(100);
            totais.setSpacingBefore(10f);

            addItemToTable(totais, "Valor Total de Serviços:", totalServicos, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(totais, "Despesas Complementares (Custos + Lucro):", despesasComplementares, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(totais, "Desconto Concedido:", desconto, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(totais, "Subtotal com Desconto:", subtotalComDesconto, BaseColor.LIGHT_GRAY, BaseColor.BLACK);
            addItemToTable(totais, "Impostos:", impostos, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(totais, "VALOR FINAL DO PROJETO:", valorFinal, BaseColor.ORANGE, BaseColor.BLACK);

            document.add(totais);
            document.add(new Paragraph(" "));

            // Assinatura
            PdfPTable assinaturaTable = new PdfPTable(1);
            assinaturaTable.setWidthPercentage(50);
            assinaturaTable.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell linha = new PdfPCell(new Phrase("__________________________"));
            linha.setBorder(Rectangle.NO_BORDER);
            linha.setHorizontalAlignment(Element.ALIGN_CENTER);
            assinaturaTable.addCell(linha);

            PdfPCell nome = new PdfPCell(new Phrase("Sindomar Cardoso"));
            nome.setBorder(Rectangle.NO_BORDER);
            nome.setHorizontalAlignment(Element.ALIGN_CENTER);
            assinaturaTable.addCell(nome);

            document.add(assinaturaTable);

            // Data
            LocalDate hoje = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Paragraph data = new Paragraph("Belém-PA, " + hoje.format(formatter), new Font(Font.FontFamily.HELVETICA, 12));
            data.setAlignment(Element.ALIGN_CENTER);
            document.add(data);

            document.close();
            System.out.println("PDF gerado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ⚠️ Agora está dentro da classe corretamente!
    private void addItemToTable(PdfPTable table, String label, String value, BaseColor backgroundColor, BaseColor textColor) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, textColor)));
        cellLabel.setBackgroundColor(backgroundColor);
        cellLabel.setPadding(5);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, textColor)));
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellValue.setBackgroundColor(backgroundColor);
        cellValue.setPadding(5);
        table.addCell(cellValue);
    }
}
