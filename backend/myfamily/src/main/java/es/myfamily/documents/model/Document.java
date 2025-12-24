package es.myfamily.documents.model;

import java.time.LocalDate;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "document_id")
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  @Column(name = "file_type", nullable = false)
  private String fileType;

  @Column(name = "file_size", nullable = false)
  private Long fileSize;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private DocumentCategory category;

  @Column(name = "family_member_user_id", nullable = false)
  private Long familyMemberUserId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "family_id", referencedColumnName = "family_id", insertable = false, updatable = false),
      @JoinColumn(name = "family_member_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
  })
  private FamilyMember familyMember;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "family_id", nullable = false)
  private Family family;

  @Column(name = "upload_date", nullable = false)
  private LocalDate uploadDate;

  @PrePersist
  public void onCreate() {
    if (this.uploadDate == null) {
      this.uploadDate = LocalDate.now();
    }
  }
}
