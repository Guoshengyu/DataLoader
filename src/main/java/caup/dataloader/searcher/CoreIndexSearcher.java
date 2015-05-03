package caup.dataloader.searcher;

import caup.dataloader.learner.ResultSetReader;
import caup.dataloader.learner.model.ResultSetElement;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.*;

/**
 * Created by Richard on 2015/03/25 .
 */
public class CoreIndexSearcher {

    static int TOP_SCORE_YEARBOOK_INDEX = 20;
    Directory indexDirectory;
    String DBIndex;
    String region;
    List<ResultSetElement> historyResult;

    public CoreIndexSearcher(Directory _indexDirectory, String _region, String _databaseIndex,  List<ResultSetElement> _historyResult ) {
        indexDirectory = _indexDirectory;
        region = _region;
        DBIndex = _databaseIndex;
        historyResult = _historyResult;
    }

    /**
     * @throws java.lang.Exception
     * @Return Top yearbook indexes based on searching
     */
    public List<String> getTopYearbookIndex() throws Exception {
        DirectoryReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Similarity sim = new DefaultSimilarity(){
            @Override
            public float idf(long docFreq,long numDocs){return 1.0f;}
        };
        searcher.setSimilarity(sim);
        QueryParser queryParser = new QueryParser(Version.LUCENE_43, IndexDirectoryGenerator.FIELD_NAME, new IKAnalyzer());
        ResultSetReader resultSetReader = new ResultSetReader();
        TopDocs topDocs = getTopDocByDBIndex(searcher, queryParser);
        List<String> historyList = resultSetReader.getHistoryYBSelectedIndex(historyResult, DBIndex);
        List<ScoreDoc> topScoreDocs = null;
        if(historyList != null) {
            List<TopDocs> resultDocsList = new ArrayList<TopDocs>();
            resultDocsList.add(topDocs);
            for (String historyResult : historyList)
                resultDocsList.add(getTopDocByHistoryResult(searcher, queryParser, historyResult));
            topScoreDocs = mergeSearchResult(searcher, resultDocsList);
        } else {
            topScoreDocs = Arrays.asList(topDocs.scoreDocs);
        }
        List<String> ret = new ArrayList<String>();
        for (ScoreDoc match : topScoreDocs) {
            Document document = searcher.doc(match.doc);
            ret.add(document.getField(IndexDirectoryGenerator.FIELD_NAME).stringValue());
        }
        return ret;
    }

    private TopDocs getTopDocByDBIndex(IndexSearcher searcher, QueryParser queryParser) throws Exception{
        Query query = safe_query_parser(queryParser, DBIndex);
        return searcher.search(query, TOP_SCORE_YEARBOOK_INDEX);
    }
    private TopDocs getTopDocByHistoryResult(IndexSearcher searcher, QueryParser queryParser, String historyResult) throws  Exception{
        Query query = safe_query_parser(queryParser, historyResult);
        return searcher.search(query, TOP_SCORE_YEARBOOK_INDEX / 2);
    }
    private List<ScoreDoc> mergeSearchResult(IndexSearcher searcher, List<TopDocs> results) throws  Exception{
        List<ScoreDoc> scoreDocList = new ArrayList<ScoreDoc>();
        for(TopDocs docs: results)
            scoreDocList.addAll(Arrays.asList(docs.scoreDocs));
        scoreDocList = mergeAndSortScoreDoc(searcher, scoreDocList);
        int end = (TOP_SCORE_YEARBOOK_INDEX < scoreDocList.size()) ? TOP_SCORE_YEARBOOK_INDEX: scoreDocList.size();
        return scoreDocList.subList(0, end);
    }

    private  List<ScoreDoc> mergeAndSortScoreDoc(IndexSearcher searcher, List<ScoreDoc> _scoreDocList) throws Exception{
        ScoreDoc[] _scoreDocArray = new ScoreDoc[_scoreDocList.size()];
        ScoreDoc[] scoreDocArray = quicksort(_scoreDocList.toArray(_scoreDocArray), 0, _scoreDocList.size() - 1);
        List<ScoreDoc> scoreDocList = new ArrayList<ScoreDoc>(Arrays.asList(scoreDocArray));
        for(int i = 0; i < scoreDocList.size(); ++i){
            Document doc1 = searcher.doc(scoreDocList.get(i).doc);
            for(int j = i + 1; j < scoreDocList.size(); ++j){
                Document doc2 = searcher.doc(scoreDocList.get(j).doc);
                if(doc1.getField(IndexDirectoryGenerator.FIELD_NAME).stringValue().equals(doc2.getField(IndexDirectoryGenerator.FIELD_NAME).stringValue())){
                    scoreDocList.remove(j);
                }
            }
        }
        return scoreDocList;
    }

    private ScoreDoc[] quicksort(ScoreDoc[] scoreDocArray, int start, int end){
        if(end > start){
            int middle = partition(scoreDocArray, start, end);
            quicksort(scoreDocArray, start, middle - 1);
            quicksort(scoreDocArray, middle + 1, end);
        }
        return scoreDocArray;
    }

    private int partition(ScoreDoc[] scoreDocArray, int start, int end){
        ScoreDoc x = (ScoreDoc)scoreDocArray[end];
        int i = start - 1;
        for (int j = start; j < end; ++j){
            if(((ScoreDoc)scoreDocArray[j]).score >= x.score){
                i++;
                exchange(scoreDocArray, i, j);
            }
        }
        exchange(scoreDocArray, i + 1, end);
        return i + 1;
    }

    private void exchange(Object[] scoreDocArray, int p, int q){
        ScoreDoc temp = (ScoreDoc)scoreDocArray[p];
        scoreDocArray[p] = scoreDocArray[q];
        scoreDocArray[q] = temp;
    }
    private Query safe_query_parser(QueryParser qp, String raw_query) throws org.apache.lucene.queryparser.classic.ParseException {
        Query q;
        try {
            q = qp.parse(raw_query);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            q = null;
        }
        if (q == null) {
            String cooked;
            // consider changing this "" to " "
            cooked = raw_query.replace("(", " ");
            cooked = cooked.replace(")", " ");
            cooked = cooked.replace("/", " ");
            cooked = cooked.replace(":", " ");
            System.out.println("Parsing ERROR!!   " + cooked);
            q = qp.parse(cooked);
        }
        return q;
    }

}
