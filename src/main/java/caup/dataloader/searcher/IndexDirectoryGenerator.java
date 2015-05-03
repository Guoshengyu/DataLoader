package caup.dataloader.searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.List;

/**
 * Created by Richard on 2015/05/03 .
 */
public class IndexDirectoryGenerator {

    public static String FIELD_NAME = "index-name";
    /*
     *
     *  We use RAM directory(index in memory) to create index due to the small size of text yearbook Index
     *
     *  @throws java.io.IOException
     */
    public static Directory createIndexDirectory( List<String> yearBookIndexList) throws IOException{
        RAMDirectory directory = new RAMDirectory();
        IKAnalyzer analyzer = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, iwc);
        for (String text : yearBookIndexList) {
            Document document = new Document();
            document.add(new TextField(FIELD_NAME, text.trim(), Field.Store.YES));
            indexWriter.addDocument(document, analyzer);
        }
        indexWriter.close();
        return directory;
    }



}
