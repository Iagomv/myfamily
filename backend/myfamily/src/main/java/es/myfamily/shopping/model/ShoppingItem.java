package es.myfamily.shopping.model;

import java.util.Date;

import org.springframework.security.access.method.P;

import es.myfamily.families.model.Family;
import es.myfamily.family_member.model.FamilyMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "shopping_items")
public class ShoppingItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "family_id", nullable = false)
  private Family family;

  @Column(name = "item_name", nullable = false)
  private String itemName;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private ShoppingCategory category;

  @Column(name = "is_purchased", nullable = false)
  private Boolean isPurchased;

  @Column(name = "added_by_user_id")
  private Long addedByUserId;

  @Column(name = "added_date", nullable = false)
  private Date addedDate;

  @Column(name = "bought_date")
  private Date boughtDate;

  @Column(name = "bought_by_user_id")
  private Long boughtByUserId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumns({
      @JoinColumn(name = "family_id", referencedColumnName = "family_id", insertable = false, updatable = false),
      @JoinColumn(name = "added_by_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
  })
  private FamilyMember addedBy;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @PrePersist
  protected void onCreate() {
    this.addedDate = new Date();
    this.isDeleted = this.isDeleted == null ? false : this.isDeleted;

  }
}
