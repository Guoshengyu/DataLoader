package caup.dataloader.dao;

import caup.dataloader.entity.DimIndicator3Entity;
import caup.dataloader.util.DataWrapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Richard on 2015/03/21 .
 */
@Repository
public class DimIndicator3Dao extends BaseDao<DimIndicator3Entity>{

    public DataWrapper<List<DimIndicator3Entity>> getIndicatorList(){

        DataWrapper<List<DimIndicator3Entity>> ret = new DataWrapper<List<DimIndicator3Entity>>();
        Session session = getSession();
        Criteria criteria = session.createCriteria(DimIndicator3Entity.class);
        List<DimIndicator3Entity> dimIndicator3EntityList = (List<DimIndicator3Entity>)criteria.list();
      //  System.out.println(dimIndicator3EntityList.get(0).getId());

        ret.setData(dimIndicator3EntityList);
        return ret;
    }
}
