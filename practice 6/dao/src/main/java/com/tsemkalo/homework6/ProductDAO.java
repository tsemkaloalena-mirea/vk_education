package com.tsemkalo.homework6;

import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import static generated.Tables.PRODUCT;

public final class ProductDAO extends AbstractDAO<ProductRecord> {
    @NotNull
    private final DSLContext context;

    public ProductDAO(@NotNull DSLContext context) {
        super(context, PRODUCT, PRODUCT.ID);
        this.context = context;
    }

//    public List<JsonObject> getProductsTotalForPeriod(LocalDate fromDate, LocalDate toDate) {
//        List<JsonObject> products = new ArrayList<>();
//        String query = "select i.invoice_date, item.product_id, sum(item.amount) as amount, sum(item.cost) as cost, sum(item.amount * item.cost) as total from invoice_item as item\n" +
//                "inner join invoice as i on item.invoice_id = i.id\n" +
//                "where i.invoice_date between ? and ?\n" +
//                "group by i.invoice_date, item.product_id order by i.invoice_date;";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            int fieldIndex = 1;
//            preparedStatement.setDate(fieldIndex++, Date.valueOf(fromDate));
//            preparedStatement.setDate(fieldIndex, Date.valueOf(toDate));
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                JsonObject productReport = new JsonObject();
//                productReport.addProperty("invoiceDate", resultSet.getDate("invoice_date").toString());
//                productReport.addProperty("productId", resultSet.getLong("product_id"));
//                productReport.addProperty("amount", resultSet.getInt("amount"));
//                productReport.addProperty("cost", resultSet.getInt("cost"));
//                productReport.addProperty("total", resultSet.getInt("total"));
//                products.add(productReport);
//            }
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//        }
//        return products;
//    }
//
//    public List<JsonObject> getProductsAverageCostForPeriod(LocalDate fromDate, LocalDate toDate) {
//        List<JsonObject> products = new ArrayList<>();
//        String query = "select item.product_id, avg(cost) as average_cost from invoice_item as item\n" +
//                "inner join invoice as i on item.invoice_id = i.id\n" +
//                "where i.invoice_date between ? and ?\n" +
//                "group by item.product_id;";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            int fieldIndex = 1;
//            preparedStatement.setDate(fieldIndex++, Date.valueOf(fromDate));
//            preparedStatement.setDate(fieldIndex, Date.valueOf(toDate));
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                JsonObject productAverageCost = new JsonObject();
//                productAverageCost.addProperty("product_id", resultSet.getLong("product_id"));
//                productAverageCost.addProperty("average_cost", resultSet.getDouble("average_cost"));
//                products.add(productAverageCost);
//            }
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//        }
//        return products;
//    }
}
