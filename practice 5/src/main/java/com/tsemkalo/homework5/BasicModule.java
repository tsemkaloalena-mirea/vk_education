//package com.tsemkalo.homework5;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.name.Names;
//import com.tsemkalo.homework5.configuration.JDBCCredentials;
//import com.tsemkalo.homework5.dao.AbstractDAO;
//import com.tsemkalo.homework5.dao.InvoiceDAO;
//import com.tsemkalo.homework5.dao.OrganisationDAO;
//import com.tsemkalo.homework5.dao.ProductDAO;
//import com.tsemkalo.homework5.entity.Organisation;
//import com.tsemkalo.homework5.entity.Product;
//import com.tsemkalo.homework5.entity.mapper.InvoiceMapper;
//import com.tsemkalo.homework5.entity.mapper.Mapper;
//import com.tsemkalo.homework5.entity.mapper.OrganisationMapper;
//import com.tsemkalo.homework5.entity.mapper.ProductMapper;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public final class BasicModule extends AbstractModule {
//    private static final @NotNull
//    JDBCCredentials CREDENTIALS = JDBCCredentials.DEFAULT;
////    private final String confFileName;
////    public BasicModule(String confFileName) {
////        this.confFileName = confFileName;
////    }
//
//    @Override
//    protected void configure() {
//        try (Connection connection = DriverManager.getConnection(CREDENTIALS.url(), CREDENTIALS.login(), CREDENTIALS.password())) {
//            bind(Connection.class).toInstance(connection);
//
//            bind(Mapper.class).annotatedWith(Names.named("Product")).to(ProductMapper.class);
////            bind(Mapper.class).annotatedWith(Names.named("Organisation")).to(OrganisationMapper.class);
////            bind(Mapper.class).annotatedWith(Names.named("Invoice")).to(InvoiceMapper.class);
//
//            bind(AbstractDAO.class).annotatedWith(Names.named("Product")).to(ProductDAO.class);
////            bind(AbstractDAO.class).annotatedWith(Names.named("Organisation")).to(OrganisationDAO.class);
////            bind(AbstractDAO.class).annotatedWith(Names.named("Invoice")).to(InvoiceDAO.class);
////            bind(AbstractDAO.class).annotatedWith(Names.named("ProductDAO")).to(ProductDAO.class);
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//            System.exit(-1);
//        }
//
//
//
//
//
//    }
//
////    private void registerConfigFile() {
////        try {
////            Properties properties = new Properties();
////            Path filePath = Paths.get(this.getClass().getResource(confFileName).toURI());
////            FileInputStream fileInputStream = new FileInputStream(filePath.toString());
////            properties.load(fileInputStream);
////            Names.bindProperties(binder(), properties);
////            fileInputStream.close();
////        } catch (IOException | URISyntaxException exception) {
////            log.error(Arrays.toString(exception.getStackTrace()));
////            System.exit(-1);
////        }
////    }
//}