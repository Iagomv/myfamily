package es.myfamily.documents;

import static org.assertj.core.api.Assertions.assertThat;

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

import es.myfamily.documents.model.DocumentCategoryRequestDto;
import es.myfamily.documents.repository.DocumentCategoryRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "JWT_SECRET=testsecret"
})
@AutoConfigureMockMvc
public class DocumentCategoryIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DocumentCategoryRepository docCategoryRepo;

  @Autowired
  private es.myfamily.users.repository.UsersRepository usersRepo;

  @Autowired
  private es.myfamily.families.repository.FamilyRepository familyRepo;

  @Autowired
  private es.myfamily.family_member.repository.FamilyMemberRepository familyMemberRepo;

  private org.springframework.security.core.Authentication auth;

  @BeforeEach
  void setUp() {
    // create user
    es.myfamily.users.model.Users user = new es.myfamily.users.model.Users();
    user.setEmail("test@example.com");
    user.setUsername("testuser");
    user.setPassword_hash("x");
    usersRepo.save(user);

    // create family
    es.myfamily.families.model.Family family = new es.myfamily.families.model.Family();
    family.setFamilyName("TestFamily");
    family.setInvitationCode("ABC12345");
    family = familyRepo.save(family);

    // add family member
    es.myfamily.family_member.model.FamilyMemberId fmId = new es.myfamily.family_member.model.FamilyMemberId();
    fmId.setFamilyId(family.getId());
    fmId.setUserId(user.getId());
    es.myfamily.family_member.model.FamilyMember fm = new es.myfamily.family_member.model.FamilyMember();
    fm.setId(fmId);
    fm.setFamilyMemberName("Tester");
    fm.setSelectedIcon("chibi1");
    fm.setFamily(family);
    fm.setUser(user);
    familyMemberRepo.save(fm);

    // set authentication principal so SecurityUtils picks it up
    this.auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
        new es.myfamily.config.security.UserPrincipal(user.getId(), user.getEmail(), "x"), null, java.util.Collections.emptyList());
    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(this.auth);
  }

  @AfterEach
  void tearDown() {
    org.springframework.security.core.context.SecurityContextHolder.clearContext();
    familyMemberRepo.deleteAll();
    familyRepo.deleteAll();
    usersRepo.deleteAll();
  }

  @Test
  public void createCategory_withAccentedName_succeeds() throws Exception {
    // find family id
    Long familyId = familyRepo.findAll().get(0).getId();

    DocumentCategoryRequestDto dto = new DocumentCategoryRequestDto();
    dto.setName("Facturas_ñ");
    dto.setDescription("Categoría para facturas con ñ");

    MvcResult res = mockMvc.perform(post(String.format("/document-categories/%d", familyId))
        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication(this.auth))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andReturn();

    // Ensure it's persisted
    boolean exists = docCategoryRepo.findAllByFamilyId(familyId).stream()
        .anyMatch(c -> c.getName().equals(dto.getName()));
    assertThat(exists).isTrue();
  }
}