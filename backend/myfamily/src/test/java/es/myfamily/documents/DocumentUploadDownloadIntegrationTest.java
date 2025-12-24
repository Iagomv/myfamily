package es.myfamily.documents;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.myfamily.documents.model.DocumentRequest;
import es.myfamily.documents.repository.DocumentRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "app.upload.dir=target/test-uploads",
    "JWT_SECRET=testsecret"
})
@AutoConfigureMockMvc
public class DocumentUploadDownloadIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DocumentRepository docRepo;

  @Autowired
  private es.myfamily.users.repository.UsersRepository usersRepo;

  @Autowired
  private es.myfamily.families.repository.FamilyRepository familyRepo;

  @Autowired
  private es.myfamily.family_member.repository.FamilyMemberRepository familyMemberRepo;

  @Autowired
  private es.myfamily.documents.repository.DocumentCategoryRepository docCatRepo;

  private org.springframework.security.core.Authentication auth;

  @BeforeEach
  void setUp() throws Exception {
    // create user
    es.myfamily.users.model.Users user = new es.myfamily.users.model.Users();
    user.setEmail("u2@example.com");
    user.setUsername("u2");
    user.setPassword_hash("x");
    usersRepo.save(user);

    // create family
    es.myfamily.families.model.Family family = new es.myfamily.families.model.Family();
    family.setFamilyName("TestFamily2");
    family.setInvitationCode("DEF12345");
    family = familyRepo.save(family);

    // add family member
    es.myfamily.family_member.model.FamilyMemberId fmId = new es.myfamily.family_member.model.FamilyMemberId();
    fmId.setFamilyId(family.getId());
    fmId.setUserId(user.getId());
    es.myfamily.family_member.model.FamilyMember fm = new es.myfamily.family_member.model.FamilyMember();
    fm.setId(fmId);
    fm.setFamilyMemberName("Uploader");
    fm.setSelectedIcon("chibi1");
    fm.setFamily(family);
    fm.setUser(user);
    familyMemberRepo.save(fm);

    // set auth
    this.auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
        new es.myfamily.config.security.UserPrincipal(user.getId(), user.getEmail(), "x"), null, java.util.Collections.emptyList());
    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(this.auth);
  }

  @AfterEach
  void tearDown() throws Exception {
    org.springframework.security.core.context.SecurityContextHolder.clearContext();
    docRepo.deleteAll();
    familyMemberRepo.deleteAll();
    familyRepo.deleteAll();
    usersRepo.deleteAll();
    // cleanup uploads
    Path t = Path.of("target/test-uploads");
    if (Files.exists(t)) {
      Files.walk(t).sorted(java.util.Comparator.reverseOrder()).map(Path::toFile).forEach(java.io.File::delete);
    }
  }

  @Test
  public void uploadAndDownloadDocument_succeeds() throws Exception {
    Long familyId = familyRepo.findAll().get(0).getId();
    // create category
    es.myfamily.documents.model.DocumentCategory cat = new es.myfamily.documents.model.DocumentCategory();
    cat.setName("Receipts");
    cat.setFamilyId(familyId);
    cat = docCatRepo.save(cat);
    // prepare simple file bytes
    byte[] bytes = "hello world".getBytes();
    Byte[] boxed = new Byte[bytes.length];
    for (int i = 0; i < bytes.length; i++)
      boxed[i] = bytes[i];

    DocumentRequest req = new DocumentRequest();
    req.setTitle("receipt1");
    req.setCategoryId(cat.getId());
    req.setFileData(boxed);

    MvcResult res = mockMvc.perform(post(String.format("/documents/%d", familyId))
        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication(this.auth))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andReturn();

    int status = res.getResponse().getStatus();
    if (status != 200) {
      throw new AssertionError("Upload failed (status=" + status + "): " + res.getResponse().getContentAsString());
    }

    List<es.myfamily.documents.model.Document> docs = docRepo.findAllByFamilyId(familyId);
    assertThat(docs).isNotEmpty();
    es.myfamily.documents.model.Document saved = docs.get(0);

    // file should exist on disk
    Path p = Path.of("target/test-uploads").resolve(saved.getFilePath().replaceFirst("^/", ""));
    assertThat(Files.exists(p)).isTrue();

    // download
    MvcResult dl = mockMvc.perform(get(String.format("/documents/%d/download", saved.getId()))
        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication(this.auth)))
        .andExpect(status().isOk())
        .andReturn();

    byte[] body = dl.getResponse().getContentAsByteArray();
    assertThat(body).isEqualTo(bytes);
  }
}