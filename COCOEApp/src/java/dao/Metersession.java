package dao;
// Generated Apr 17, 2018 9:02:26 PM by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Metersession generated by hbm2java
 */
@Entity
@Table(name="metersession"
    ,catalog="cocoedb"
    , uniqueConstraints = @UniqueConstraint(columnNames="code") 
)
public class Metersession  implements java.io.Serializable {


     private Integer id;
     private int areaid;
     private String header;
     private String code;
     private String status;
     private Date createdDate;

    public Metersession() {
    }

    public Metersession(int areaid, String header, String code, String status, Date createdDate) {
       this.areaid = areaid;
       this.header = header;
       this.code = code;
       this.status = status;
       this.createdDate = createdDate;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="areaid", nullable=false)
    public int getAreaid() {
        return this.areaid;
    }
    
    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    
    @Column(name="header", nullable=false, length=128)
    public String getHeader() {
        return this.header;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }

    
    @Column(name="code", unique=true, nullable=false, length=12)
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    
    @Column(name="status", nullable=false, length=20)
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdDate", nullable=false, length=19)
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }




}

