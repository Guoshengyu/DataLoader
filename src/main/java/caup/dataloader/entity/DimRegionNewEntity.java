package caup.dataloader.entity;

import javax.persistence.*;

/**
 * Created by Richard on 2015/03/21 .
 */
@Entity
@Table(name = "dim_region_new", schema = "dbo", catalog = "ssb")
public class DimRegionNewEntity {
    private double id;
    private String name;
    private Double pid;

    @Id
    @Column(name = "id")
    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "pid")
    public Double getPid() {
        return pid;
    }

    public void setPid(Double pid) {
        this.pid = pid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DimRegionNewEntity that = (DimRegionNewEntity) o;

        if (Double.compare(that.id, id) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(id);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        return result;
    }
}
