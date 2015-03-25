package caup.dataloader.transformer.reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 2015/03/23 .
 */
public class LuceneTest {

    public static void main(String[] args) {
        LuceneTest test = new LuceneTest();
        test.test();
    }

    public void test() {
        try {

            DirectoryReader reader = DirectoryReader.open(getIndexDirectory());
            IndexSearcher searcher = new IndexSearcher(reader);
            // searcher.setSimilarity(new LMSimilarity());

            String keyword = "地区生产总值";

            QueryParser queryParser = new QueryParser(Version.LUCENE_43, "contents", new IKAnalyzer());
            Query query = queryParser.parse(keyword);
            TopDocs topDocs = searcher.search(query, 10);
            for (ScoreDoc match : topDocs.scoreDocs) {
                Explanation explanation = searcher.explain(query, match.doc);
                System.out.println("------------");
                Document document = searcher.doc(match.doc);
                System.out.println(document.getField("contents").stringValue());
                System.out.println(explanation.toString());
            }
            reader.close();
            /*IKAnalyzer analyzer = new IKAnalyzer(true);
            String text = "这是一个测试用的句子和例子";
            List<String> stopwords = new ArrayList<String>();
            stopwords.add("和例子");

          //  System.out.println(Get().);
            Dictionary.initial(DefaultConfig.getInstance());
            Dictionary.getSingleton().addWords(stopwords);
            System.out.println(DefaultConfig.getInstance().getExtStopWordDictionarys());
            TokenStream ts = analyzer.tokenStream("field", new StringReader(text));
            CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            while (ts.incrementToken()) {
                System.out.println(term.toString());
            }
            ts.end();
            ts.close();*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Directory getIndexDirectory() throws Exception{
        String[] yearBookindex = {"全市生产总值_地区生产总值","全市生产总值_第一产业增加值","全市生产总值_第二产业增加值", "全市生产总值_第二产业增加值_工业增加值",
                "全市生产总值_第二产业增加值_建筑业增加值 ",
                "全市生产总值_第三产业增加值 ",
                "全市生产总值_人均生产总值（按户籍） ",
                "全市生产总值_人均生产总值（按常住） ",
                "全市生产总值增长率_地区生产总值 ",
                "全市生产总值增长率_第一产业增加值 ",
                " 全市生产总值增长率_第二产业增加值 ",
                "全市生产总值增长率_第二产业增加值_工业增加值 ",
                "全市生产总值增长率_第二产业增加值_建筑业增加值 ",
                "全市生产总值增长率_第三产业增加值 ",
                "全市生产总值增长率_人均生产总值（按户籍） "
                ,"全市生产总值增长率_人均生产总值（按常住） "
                ," 全市生产总值指数_地区生产总值 "
                ," 全市生产总值指数_第一产业增加值 "
                ,"全市生产总值指数_第二产业增加值 "
                ,"全市生产总值指数_第二产业增加值_工业增加值 "
                ,"全市生产总值指数_第二产业增加值_建筑业增加值 "
                ,"全市生产总值指数_第三产业增加值 "
                ,"全市生产总值指数_人均生产总值（按户籍） "
                ,"全市生产总值指数_人均生产总值（按常住） "
                ,"全市生产总值构成_地区生产总值 "
                ,"全市生产总值构成_第一产业增加值 "
                ,"全市生产总值构成_第二产业增加值 "
                ,"全市生产总值构成_第二产业增加值_工业增加值 "
                ,"全市生产总值构成_第二产业增加值_建筑业增加值 "
                ,"全市生产总值构成_第三产业增加值 "
                ,"分地区生产总值 "
                ,"分地区第一产业增加值 "
                ,"分地区第二产业增加值 "
                ,"分地区工业增加值 "
                ,"分地区第三产业增加值 "
                ,"分地区人均生产总值 "
                ,"分地区生产总值增长率 "
                ,"分地区第一产业增加值增长率 "
                ,"分地区第二产业增加值增长率 "
                ,"分地区工业增加值增长率 "
                ,"分地区第三产业增加值增长率 "
                ,"分地区人均GDP增长率 "
                ,"分地区生产总值发展指数 "
                ,"分地区第一产业发展指数 "
                ,"分地区第二产业发展指数 "
                ,"分地区工业增加值发展指数 "
                ,"分地区第三产业增加值发展指数 "
                ,"分地区人均GDP发展指数 "
        };
        RAMDirectory directory = new RAMDirectory();

        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, new IKAnalyzer(true));
        IndexWriter indexWriter = new IndexWriter(directory, iwc);

        Document doc = null;
        for(String text: yearBookindex){
            doc = new Document();
            doc.add(new TextField("contents", text.trim(), Field.Store.YES));
            indexWriter.addDocument(doc, new IKAnalyzer(true));
        }



        indexWriter.close();
        return directory;
    }
}
