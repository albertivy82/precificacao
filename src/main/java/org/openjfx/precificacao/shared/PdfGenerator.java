package org.openjfx.precificacao.shared;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
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

            // Título do Documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Relatório de Custos do Projeto", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Espaço
            document.add(new Paragraph(" "));

            // Adicionar tabela de etapas e atividades
            for (String etapa : agrupados.keySet()) {
                // Adicionar título da Etapa
                Font etapaFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
                document.add(new Paragraph("Etapa: " + etapa, etapaFont));

                // Criar tabela para atividades e valores
                PdfPTable table = new PdfPTable(2); // Duas colunas
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Cabeçalhos da tabela
                PdfPCell cellAtividade = new PdfPCell(new Phrase("Atividade"));
                PdfPCell cellValor = new PdfPCell(new Phrase("Valor"));
                table.addCell(cellAtividade);
                table.addCell(cellValor);

                // Adicionar atividades e valores
                Map<String, List<DetalhamentoDTO>> atividades = agrupados.get(etapa);
                for (String atividade : atividades.keySet()) {
                    PdfPCell atividadeCell = new PdfPCell(new Phrase(atividade));
                    table.addCell(atividadeCell);

                    float subtotalAtividade = atividades.get(atividade)
                            .stream()
                            .map(DetalhamentoDTO::getHoras)
                            .reduce(0f, Float::sum);
                    PdfPCell valorCell = new PdfPCell(new Phrase(FormatadorMoeda.formatarValorComoMoeda(subtotalAtividade)));
                    valorCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(valorCell);
                }

                document.add(table);
            }

            // Espaço antes dos valores totais
            document.add(new Paragraph(" "));

            // Adicionar os itens seguintes (labels e valores)
            PdfPTable totalsTable = new PdfPTable(2); // Duas colunas
            totalsTable.setWidthPercentage(100);
            totalsTable.setSpacingBefore(10f);
            totalsTable.setSpacingAfter(10f);

            // Adicionar cada item (Label na coluna 1, Valor na coluna 2)
            addItemToTable(totalsTable, "Valor Total de Serviços:", totalServicos);
            addItemToTable(totalsTable, "Total de Custos Variáveis:", totalCustosVariaveis);
            addItemToTable(totalsTable, "Custos Fixos lançados:", custosFixosLancados);
            addItemToTable(totalsTable, "Total de Impostos:", totalImpostos);
            addItemToTable(totalsTable, "Quota sobre serviços:", quotaServicos);
            addItemToTable(totalsTable, "Preço Total do Projeto:", precoTotalProjeto);

            // Adiciona a tabela de totais no documento
            document.add(totalsTable);

            // Fechar o documento
            document.close();
            System.out.println("PDF gerado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addItemToTable(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label));
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value));
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellValue);
    }
}

