package caup.dataloader.dao;

import caup.dataloader.entity.DimIndicator3Entity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Richard on 2015/03/21 .
 */
@Repository
public class DimIndicator3Dao extends BaseDao<DimIndicator3Entity>{

    public List<DimIndicator3Entity> getIndicatorList(){
        Session session = getSession();
        Criteria criteria = session.createCriteria(DimIndicator3Entity.class);
        List<DimIndicator3Entity> dimIndicator3EntityList = (List<DimIndicator3Entity>)criteria.list();
        System.out.println(dimIndicator3EntityList.get(0).getId());


        return  dimIndicator3EntityList;
    }
}
