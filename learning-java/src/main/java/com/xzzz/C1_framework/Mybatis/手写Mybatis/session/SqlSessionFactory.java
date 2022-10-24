package com.xzzz.C1_framework.Mybatis.手写Mybatis.session;

import com.xzzz.C1_framework.Mybatis.手写Mybatis.config.Configuration;
import com.xzzz.C1_framework.Mybatis.手写Mybatis.config.MapperStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class SqlSessionFactory {
    //1.初始化配置信息

    //2.生产sqlSession

    private Configuration conf = new Configuration();

    public SqlSessionFactory(){
        super();
        loadDBInfo();
        loadMappersInfo();
    }

    public static final String MAPPER_CONFIG_LOCATION = "mapper";
    public static final String DB_CONFIG_FILE = "jdbc.properties";

    /**
     * 加载数据库配置
     */
    private void loadDBInfo(){
        InputStream dbin = SqlSessionFactory.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE);
        Properties p = new Properties();
        try {
            p.load(dbin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        conf.setJdbcDriver(p.get("jdbc.driver").toString());
        conf.setJdbcUrl(p.get("jdbc.url").toString());
        conf.setJdbcUsername(p.get("jdbc.username").toString());
        conf.setJdbcPassword(p.get("jdbc.password").toString());
    }

    /**
     * 加载指定文件夹下的所有mapper.xml文件
     */
    private void loadMappersInfo(){
        URL resources = null;
        resources = SqlSessionFactory.class.getClassLoader().getResource(MAPPER_CONFIG_LOCATION);
        File mappers = new File(resources.getFile());
        if(mappers.isDirectory()){
            File[] listFiles =  mappers.listFiles();
            for(File file : listFiles){
                loadMapperInfo(file);
            }
        }
    }


    /**
     * 加载指定的mapper.xml文件
     * @param file dao.xml文件路径
     */
    private void loadMapperInfo(File file){
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获取根节点
        Element root = document.getRootElement();
        //获取命名空间
        String namespace = root.attribute("namespace").getData().toString();

        List<Element> selects = root.elements("select");
        for(Element element : selects){
            MapperStatement mapperStatement = new MapperStatement();
            String id = element.attribute("id").getData().toString();
            String resultlType = element.attribute("resultType").getData().toString();
            String sql = element.getData().toString();
            //对应dao中的方法
            String sourceId= namespace + "." + id;

            mapperStatement.setNamespace(namespace);
            mapperStatement.setResultType(resultlType);
            mapperStatement.setSourceId(sourceId);
            mapperStatement.setSql(sql);

            conf.getMapperStatements().put(sourceId,mapperStatement);
        }
    }


    public SqlSession OpenSession(){
        return new DefaultSqlSession(conf);
    }
}
