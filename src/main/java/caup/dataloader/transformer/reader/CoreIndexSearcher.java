package caup.dataloader.transformer.reader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 2015/03/25 .
 */
public class CoreIndexSearcher {

    static int TOP_SCORE_YEARBOOK_INDEX = 10;
    static String FIELD_NAME = "index-name";
    List<String> yearBookIndexList;
    String databaseIndex;
    public CoreIndexSearcher(){
        yearBookIndexList = new ArrayList<String>();
        databaseIndex = null;
    }
    public CoreIndexSearcher(String _databaseIndex, List<String> _yearBookIndexList){
        this.yearBookIndexList = _yearBookIndexList;
        this.databaseIndex = _databaseIndex;
    }
    public CoreIndexSearcher(String _databaseIndex, List<String> _yearBookIndexList, int _maxScoreIndex){
        this.yearBookIndexList = _yearBookIndexList;
        this.databaseIndex = _databaseIndex;
        TOP_SCORE_YEARBOOK_INDEX = _maxScoreIndex;
    }
    /**
     *
     * @Return Top yearbook indexes based on searching
     *
     * @exception java.lang.Exception
     */
    public List<String> getTopYearbookIndex() throws Exception{
        DirectoryReader reader = DirectoryReader.open(getLuceneIndexDirectory());
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser queryParser = new QueryParser(Version.LUCENE_43, FIELD_NAME, new IKAnalyzer());
        Query query = queryParser.parse(databaseIndex);
        TopDocs topDocs = searcher.search(query, TOP_SCORE_YEARBOOK_INDEX);
        List<String> ret = new ArrayList<String>();
        for(ScoreDoc match: topDocs.scoreDocs){
            Document document = searcher.doc(match.doc);
            ret.add(document.getField(FIELD_NAME).stringValue());
        }
        return  ret;
    }
    /**
     *  We use RAM directory(index in memory) to create index due to the small size of text yearbook Index
     *
     * @exception java.io.IOException
     */
    private Directory getLuceneIndexDirectory() throws IOException{
        RAMDirectory directory = new RAMDirectory();
        IKAnalyzer analyzer = new IKAnalyzer(true);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43,analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, iwc);


        for(String text: yearBookIndexList){
            Document document = new Document();
            document.add(new TextField(FIELD_NAME, text.trim(), Field.Store.YES));
            indexWriter.addDocument(document, analyzer);
        }
        indexWriter.close();
        return directory;
    }
}