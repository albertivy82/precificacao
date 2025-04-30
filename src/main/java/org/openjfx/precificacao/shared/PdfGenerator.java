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

    public void gerarPDF(String destino, Map<String, Map<String, List<DetalhamentoDTO>>> agrupados,
                         String totalServicos, String totalCustosVariaveis, String custosFixosLancados,
                         String totalImpostos, String quotaServicos, String precoTotalProjeto) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(destino));

            document.open();

            // Adicionar a imagem do cabeçalho
            Image logo = Image.getInstance(getClass().getResource("/images/sindomar_logo.png").toExternalForm());
            logo.scaleToFit(250, 100);  // Ajuste o tamanho da imagem
            logo.setAlignment(Element.ALIGN_CENTER);  // Centraliza a imagem
            document.add(logo);

            // Espaço depois do logotipo
            document.add(new Paragraph(" "));

            // Título do Documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Orçamento De Serviços", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Espaço
            document.add(new Paragraph(" "));

            // Criar uma tabela combinada para etapas, atividades e totais
            PdfPTable table = new PdfPTable(2); // Duas colunas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Adicionar etapas e atividades
            for (String etapa : agrupados.keySet()) {
                // Adicionar título da Etapa com cor de fundo
                Font etapaFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
                PdfPCell etapaCell = new PdfPCell(new Phrase(etapa, etapaFont));
                etapaCell.setColspan(2);
                etapaCell.setBackgroundColor(BaseColor.LIGHT_GRAY);  // Cor de fundo para o título da etapa
                etapaCell.setPadding(5);
                table.addCell(etapaCell);

                // Adicionar atividades e seus valores
                Map<String, List<DetalhamentoDTO>> atividades = agrupados.get(etapa);
                for (String atividade : atividades.keySet()) {
                    PdfPCell atividadeCell = new PdfPCell(new Phrase(atividade));
                    atividadeCell.setPadding(5);
                    table.addCell(atividadeCell);

                    float subtotalAtividade = atividades.get(atividade)
                            .stream()
                            .map(detalhamento -> detalhamento.getHoras() * detalhamento.getValorHoras())
                            .reduce(0f, Float::sum);
                    PdfPCell valorCell = new PdfPCell(new Phrase(FormatadorMoeda.formatarValorComoMoeda(subtotalAtividade)));
                    valorCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    valorCell.setPadding(5);
                    table.addCell(valorCell);
                }
            }

            // Espaço antes dos valores totais (opcional)
            document.add(new Paragraph(" "));

            // Adicionar totais na mesma tabela
            addItemToTable(table, "Valor Total de Serviços:", totalServicos, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(table, "Total de Custos Variáveis:", totalCustosVariaveis, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(table, "Custos Fixos lançados:", custosFixosLancados, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(table, "Total de Impostos:", totalImpostos, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(table, "Quota sobre serviços:", quotaServicos, BaseColor.WHITE, BaseColor.BLACK);
            addItemToTable(table, "Preço Total do Projeto:", precoTotalProjeto, BaseColor.ORANGE, BaseColor.BLACK); // Linha com fundo colorido

            // Adicionar a tabela combinada no documento
            document.add(table);

            // Espaço para assinatura
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Criar uma tabela para centralizar a linha de assinatura e o nome
            PdfPTable assinaturaTable = new PdfPTable(1); // Uma coluna
            assinaturaTable.setWidthPercentage(50); // Largura de 50% da página
            assinaturaTable.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Linha de assinatura
            PdfPCell linhaAssinatura = new PdfPCell(new Phrase("__________________________"));
            linhaAssinatura.setBorder(Rectangle.NO_BORDER);
            linhaAssinatura.setHorizontalAlignment(Element.ALIGN_CENTER);
            assinaturaTable.addCell(linhaAssinatura);

            // Nome da pessoa para assinatura
            PdfPCell nomeAssinatura = new PdfPCell(new Phrase("Sindormar Cardoso"));
            nomeAssinatura.setBorder(Rectangle.NO_BORDER);
            nomeAssinatura.setHorizontalAlignment(Element.ALIGN_CENTER);
            assinaturaTable.addCell(nomeAssinatura);

            document.add(assinaturaTable);

            // Adicionar local e data
            LocalDate hoje = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Paragraph data = new Paragraph("Belém-PA, " + hoje.format(formatter), new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
            data.setAlignment(Element.ALIGN_CENTER);
            document.add(data);


            // Fechar o documento
            document.close();
            System.out.println("PDF gerado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addItemToTable(PdfPTable table, String label, String value, BaseColor backgroundColor, BaseColor textColor) {
        // Cria a célula do label com cor de fundo e texto personalizados
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, textColor)));
        cellLabel.setBackgroundColor(backgroundColor);
        cellLabel.setPadding(5);
        table.addCell(cellLabel);

        // Cria a célula do valor com alinhamento à direita e cor de fundo
        PdfPCell cellValue = new PdfPCell(new Phrase(value, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, textColor)));
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellValue.setBackgroundColor(backgroundColor);
        cellValue.setPadding(5);
        table.addCell(cellValue);
    }
}

