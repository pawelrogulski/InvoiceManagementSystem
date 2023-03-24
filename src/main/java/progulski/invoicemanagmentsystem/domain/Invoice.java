package progulski.invoicemanagmentsystem.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date date;
    private String city;
    private String CompanyName;
    private String NIP;
    @OneToMany(mappedBy = "invoice")
    private Set<Items> items;
}
