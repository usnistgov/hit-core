package gov.nist.hit.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseDocument;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.CFTestPlanRepository;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.CFTestPlanService;
import gov.nist.hit.core.service.ResourceLoader;
import gov.nist.hit.core.service.TestCaseDocumentationService;
import gov.nist.hit.core.service.TestPlanService;
import gov.nist.hit.core.service.ZipGenerator;
import gov.nist.hit.core.service.util.DocumentationUtils;

@Service
public class TestCaseDocumentationServiceImpl implements TestCaseDocumentationService {

  static final Logger logger = LogManager.getLogger(TestCaseDocumentationServiceImpl.class);

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  protected TestPlanRepository testPlanRepository;


  @Autowired
  protected CFTestPlanRepository cfTestPlanRepository;


  @Autowired
  protected TestPlanService testPlanService;
  
  @Autowired
  protected CFTestPlanService cfTestPlanService;

  @Autowired
  private ZipGenerator zipGenerator;


  protected com.fasterxml.jackson.databind.ObjectMapper obm;

  public TestCaseDocumentationServiceImpl() {
    obm = new com.fasterxml.jackson.databind.ObjectMapper();
    obm.setSerializationInclusion(Include.NON_NULL);
  }



  /**
   * 
   * @param scope
   * @param preloaded
   * @param domain
   * @throws IOException
   */
  @Override
  public List<TestCaseDocumentation> generate(TestScope scope, String domain) throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    

    List<CFTestPlan> cfTestPlans =
        cfTestPlanRepository.findAllIdByStageAndScopeAndDomain(TestingStage.CF, scope, domain); 
    for (int i=0;i<cfTestPlans.size();i++) {
    	cfTestPlans.set(i,cfTestPlanService.findOne(cfTestPlans.get(i).getId()));
    } 
    
    documents.addAll(generateCfDocumentations(cfTestPlans));
       
    
    
    List<TestPlan> cbTestPlans =
        testPlanRepository.findAllIdByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
    for (int i=0;i<cbTestPlans.size();i++) {
    	cbTestPlans.set(i,testPlanService.findOne(cbTestPlans.get(i).getId()));
    } 
    documents.addAll(generateCbDocumentations(cbTestPlans));
    
    return documents;
  }

  @Override
  public List<TestCaseDocumentation> generate(TestScope scope, String domain, String username)
      throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    List<CFTestPlan> cfTestPlans = cfTestPlanRepository
        .findAllByStageAndAuthorAndScopeAndDomain(TestingStage.CF, username, scope, domain);
    documents.addAll(generateCfDocumentations(cfTestPlans));
    List<TestPlan> cbTestPlans = testPlanRepository
        .findAllByStageAndScopeAndDomainAndAuthor(TestingStage.CB, scope, domain, username);
    documents.addAll(generateCbDocumentations(cbTestPlans));
    return documents;
  }


  public List<TestCaseDocumentation> generateCbDocumentations(List<TestPlan> testPlans)
      throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    TestCaseDocumentation doc = cb("Context-based", TestingStage.CB, testPlans);
    
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      documents.add(doc);
    }
        return documents;
  }

  public List<TestCaseDocumentation> generateCfDocumentations(List<CFTestPlan> testPlans)
      throws IOException {
    List<TestCaseDocumentation> documents = new ArrayList<TestCaseDocumentation>();
    TestCaseDocumentation doc = cf("Context-free", TestingStage.CF, testPlans);
    if (doc != null) {
      doc.setJson(obm.writeValueAsString(doc));
      documents.add(doc);
    }
    return documents;
  }



  private TestCaseDocumentation cb(String title, TestingStage stage, List<TestPlan> tps)
      throws IOException {
    if (tps != null && !tps.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      for (TestPlan testPlan : tps) {
    	
        documentation.getChildren().add(generate(testPlan));       
        
      }
      return documentation;
    }
    return null;
  }

  private TestCaseDocumentation cf(String title, TestingStage stage, List<CFTestPlan> tos)
      throws IOException {
    if (tos != null && !tos.isEmpty()) {
      TestCaseDocumentation documentation = new TestCaseDocumentation();
      documentation.setTitle(title);
      documentation.setStage(stage);
      Collections.sort(tos);
      for (CFTestPlan to : tos) {
        documentation.getChildren().add(generate(to));
      }
      return documentation;
    }
    return null;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(TestPlan tp) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tp);
    doc.setId(tp.getId());
    if (tp.getTestCaseGroups() != null && !tp.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tp.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup tcg : list) {
    	  
    	  doc.getChildren().add(generate(tcg));        
        
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tp.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
    	  
        doc.getChildren().add(generate(tc));        
        
      }
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(TestCaseGroup tcg) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tcg);
    doc.setId(tcg.getId());
    if (tcg.getTestCaseGroups() != null && !tcg.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tcg.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup child : list) {
    	            
        doc.getChildren().add(generate(child));
        
      }
    }

    if (tcg.getTestCases() != null && !tcg.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tcg.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
    	            
        doc.getChildren().add(generate(tc));
        
      }
    }

    return doc;
  }


  private gov.nist.hit.core.domain.TestCaseDocument generate(CFTestStepGroup tcg)
      throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tcg);
    doc.setId(tcg.getId());
    if (tcg.getTestStepGroups() != null && !tcg.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tcg.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup child : list) {
        doc.getChildren().add(generate(child));
      }
    }
    if (tcg.getTestSteps() != null && !tcg.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tcg.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
        doc.getChildren().add(generate(tc));
      }
    }
    return doc;
  }


  private gov.nist.hit.core.domain.TestCaseDocument generate(TestCase tc) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tc);
    doc.setId(tc.getId());
    if (tc.getTestSteps() != null && !tc.getTestSteps().isEmpty()) {
      List<TestStep> list = new ArrayList<TestStep>(tc.getTestSteps());
      Collections.sort(list);
      for (TestStep ts : list) {    	
    	  
    	  doc.getChildren().add(generate(ts));
    	  
      }
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(TestStep ts) throws IOException {
	         
	gov.nist.hit.core.domain.TestCaseDocument doc;    
	if (ts.getTestContext() != null) {
		doc = resourceLoader.generateTestCaseDocument(ts.getTestContext());
    	doc = initTestCaseDocument(ts, doc);
    	doc.setId(ts.getTestContext().getId());
    }else {
    	doc = initTestCaseDocument(ts);
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(CFTestStep ts) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc =
        resourceLoader.generateTestCaseDocument(ts.getTestContext());
    doc = initTestCaseDocument(ts, doc);
    if (ts.getTestContext() != null) {
      doc.setId(ts.getTestContext().getId());
    }
    return doc;
  }

  private gov.nist.hit.core.domain.TestCaseDocument generate(CFTestPlan tp) throws IOException {
    gov.nist.hit.core.domain.TestCaseDocument doc = initTestCaseDocument(tp);
    doc.setId(tp.getId());

    if (tp.getTestStepGroups() != null && !tp.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tp.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup tcg : list) {
        doc.getChildren().add(generate(tcg));
      }
    }
    if (tp.getTestSteps() != null && !tp.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tp.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
        doc.getChildren().add(generate(tc));
      }
    }

    return doc;
  }



  private gov.nist.hit.core.domain.TestCaseDocument initTestCaseDocument(AbstractTestCase ts)
      throws IOException {
    return initTestCaseDocument(ts, new TestCaseDocument());
  }

  private gov.nist.hit.core.domain.TestCaseDocument initTestCaseDocument(AbstractTestCase ts,
      TestCaseDocument doc) throws IOException {
    doc.setTitle(ts.getName());
    doc.setType(ts.getType().toString());
    doc.setTsPath(ts.getTestStory() != null ? ts.getTestStory().getPdfPath() : null);
    if (ts instanceof TestPlan) {
      TestPlan tp = (TestPlan) ts;
      doc.setTpPath(tp.getTestPackage() != null ? tp.getTestPackage().getPdfPath() : null);
      doc.setTpsPath(tp.getTestPlanSummary() != null ? tp.getTestPlanSummary().getPdfPath() : null);
    } else if (ts instanceof TestStep) {
      TestStep tStep = (TestStep) ts;
      doc.setMcPath(
          tStep.getMessageContent() != null ? tStep.getMessageContent().getPdfPath() : null);
      doc.setTdsPath(tStep.getTestDataSpecification() != null
          ? tStep.getTestDataSpecification().getPdfPath() : null);
      doc.setJdPath(
          tStep.getJurorDocument() != null ? tStep.getJurorDocument().getPdfPath() : null);
    }
    return doc;
  }



  @Override
  public InputStream zipContextfreeMessages(TestScope scope, String domain) throws Exception {
    List<CFTestPlan> testPlans =
        cfTestPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CF, scope, domain);
    String name = "ContextFreeExampleMessages";
    Path path = Files.createTempDirectory(null);
    File rootFolder = path.toFile();
    if (!rootFolder.exists()) {
      rootFolder.mkdir();
    }
    String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
    for (CFTestPlan testPlan : testPlans) {
      DocumentationUtils.createMessageFile(testPlan, folderToZip);
    }
    String zipFilename = rootFolder + File.separator + name + ".zip";
    zipGenerator.zip(zipFilename, folderToZip);
    FileInputStream io = new FileInputStream(new File(zipFilename));
    return io;
  }

  @Override
  public InputStream zipContextbasedTestPackages(TestScope scope, String domain) throws Exception {
    String name = "ContextbasedTestPackages";
    List<TestPlan> testPlans =
        testPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
    Path path = Files.createTempDirectory(null);
    File rootFolder = path.toFile();
    if (!rootFolder.exists()) {
      rootFolder.mkdir();
    }
    String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
    for (TestPlan testPlan : testPlans) {
      DocumentationUtils.createTestPackageFile(testPlan, folderToZip);
    }
    String zipFilename = rootFolder + File.separator + name + ".zip";
    zipGenerator.zip(zipFilename, folderToZip);
    FileInputStream io = new FileInputStream(new File(zipFilename));
    return io;
  }



  @Override
  public InputStream zipContextbasedMessages(TestScope scope, String domain) throws Exception {
    String name = "ContextbasedExampleMessages";
    List<TestPlan> testPlans =
        testPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
    Path path = Files.createTempDirectory(null);
    File rootFolder = path.toFile();
    if (!rootFolder.exists()) {
      rootFolder.mkdir();
    }
    String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
    for (TestPlan testPlan : testPlans) {
      DocumentationUtils.createMessageFile(testPlan, folderToZip);
    }
    String zipFilename = rootFolder + File.separator + name + ".zip";
    zipGenerator.zip(zipFilename, folderToZip);
    FileInputStream io = new FileInputStream(new File(zipFilename));
    return io;
  }

  @Override
  public InputStream generateCompleteDomainZip(TestScope scope, String domain) throws Exception {
    Path path = Files.createTempDirectory(null);
    File rootFolder = path.toFile();
    if (!rootFolder.exists()) {
      rootFolder.mkdir();
    }
    String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
    File toZipFolder = new File(folderToZip);
    toZipFolder.mkdirs();

    // Handle GLOBALANDUSER by fetching both scopes separately
    List<TestScope> scopesToFetch = new ArrayList<>();
    if (TestScope.GLOBALANDUSER.equals(scope)) {
      scopesToFetch.add(TestScope.USER);
      scopesToFetch.add(TestScope.GLOBAL);
    } else {
      scopesToFetch.add(scope);
    }

    for (TestScope fetchScope : scopesToFetch) {
      List<CFTestPlan> cfTestPlans =
          cfTestPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CF, fetchScope, domain);
      System.out.println("DEBUG: Found " + cfTestPlans.size() + " CF test plans for domain: " + domain + ", scope: " + fetchScope);
      for (CFTestPlan cfTestPlan : cfTestPlans) {
        try {
          System.out.println("DEBUG: Processing CF test plan: " + cfTestPlan.getName());
          createCompleteDomainDocumentation(cfTestPlan, "Context-free", folderToZip, domain);
        } catch (Exception e) {
          System.out.println("DEBUG: Error processing CF test plan: " + e.getMessage());
          e.printStackTrace();
        }
      }

      List<TestPlan> cbTestPlans =
          testPlanRepository.findAllByStageAndScopeAndDomain(TestingStage.CB, fetchScope, domain);
      System.out.println("DEBUG: Found " + cbTestPlans.size() + " CB test plans for domain: " + domain + ", scope: " + fetchScope);
      for (TestPlan cbTestPlan : cbTestPlans) {
        try {
          System.out.println("DEBUG: Processing CB test plan: " + cbTestPlan.getName());
          createCompleteDomainDocumentation(cbTestPlan, "Context-based", folderToZip, domain);
        } catch (Exception e) {
          System.out.println("DEBUG: Error processing CB test plan: " + e.getMessage());
          e.printStackTrace();
        }
      }
    }

    String zipFilename = rootFolder + File.separator + domain + "-Complete-Documentation.zip";
    File[] files = toZipFolder.listFiles();
    System.out.println("DEBUG: Folder contents: " + (files == null ? "null" : files.length + " items"));
    if (files != null && files.length > 0) {
      System.out.println("DEBUG: Creating zip file...");
      zipGenerator.zip(zipFilename, folderToZip);
      File zipFile = new File(zipFilename);
      if (zipFile.exists()) {
        System.out.println("DEBUG: Zip file created successfully: " + zipFile.getAbsolutePath());
        return new FileInputStream(zipFile);
      }
    }
    System.out.println("DEBUG: No files to zip - returning null");
    return null;
  }

  private void createCompleteDomainDocumentation(CFTestPlan cfTestPlan, String testType,
      String parentDirName, String domain) throws Exception {
    String baseFolder = parentDirName + File.separator + domain + File.separator + testType
        + File.separator + cfTestPlan.getPosition() + "." + cfTestPlan.getName();
    createCFTestPlanDocumentation(cfTestPlan, baseFolder);
  }

  private void createCompleteDomainDocumentation(TestPlan cbTestPlan, String testType,
      String parentDirName, String domain) throws Exception {
    String baseFolder = parentDirName + File.separator + domain + File.separator + testType
        + File.separator + cbTestPlan.getPosition() + "." + cbTestPlan.getName();
    createCBTestPlanDocumentation(cbTestPlan, baseFolder);
  }

  private void createCFTestPlanDocumentation(CFTestPlan tp, String parentDirName) throws Exception {
    copyContainerArtifacts(tp, parentDirName);
    if (tp.getTestStepGroups() != null && !tp.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<>(tp.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup tcg : list) {
        createCFTestStepGroupDocumentation(tcg, parentDirName);
      }
    }
    if (tp.getTestSteps() != null && !tp.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<>(tp.getTestSteps());
      Collections.sort(list);
      for (CFTestStep ts : list) {
        createCFTestStepDocumentation(ts, parentDirName);
      }
    }
  }

  private void createCFTestStepGroupDocumentation(CFTestStepGroup tsg, String parentDirName) {
    String folder = parentDirName + File.separator + tsg.getPosition() + "." + tsg.getName();
    copyContainerArtifacts(tsg, folder);
    if (tsg.getTestStepGroups() != null && !tsg.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<>(tsg.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup child : list) {
        createCFTestStepGroupDocumentation(child, folder);
      }
    }
    if (tsg.getTestSteps() != null && !tsg.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<>(tsg.getTestSteps());
      Collections.sort(list);
      for (CFTestStep ts : list) {
        createCFTestStepDocumentation(ts, folder);
      }
    }
  }

  private void createCFTestStepDocumentation(CFTestStep ts, String parentDirName) {
    String folder = parentDirName + File.separator + ts.getPosition() + "." + ts.getName();
    createTestStepArtifacts(ts, folder);
  }

  private void createCBTestPlanDocumentation(TestPlan tp, String parentDirName) throws Exception {
    copyContainerArtifacts(tp, parentDirName);
    if (tp.getTestCaseGroups() != null && !tp.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<>(tp.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup tcg : list) {
        createCBTestCaseGroupDocumentation(tcg, parentDirName);
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<>(tp.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
        createCBTestCaseDocumentation(tc, parentDirName);
      }
    }
  }

  private void createCBTestCaseGroupDocumentation(TestCaseGroup tcg, String parentDirName) {
    String folder = parentDirName + File.separator + tcg.getPosition() + "." + tcg.getName();
    copyContainerArtifacts(tcg, folder);
    if (tcg.getTestCaseGroups() != null && !tcg.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<>(tcg.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup child : list) {
        createCBTestCaseGroupDocumentation(child, folder);
      }
    }
    if (tcg.getTestCases() != null && !tcg.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<>(tcg.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
        createCBTestCaseDocumentation(tc, folder);
      }
    }
  }

  private void createCBTestCaseDocumentation(TestCase tc, String parentDirName) {
    String folder = parentDirName + File.separator + tc.getPosition() + "." + tc.getName();
    copyContainerArtifacts(tc, folder);
    if (tc.getTestSteps() != null && !tc.getTestSteps().isEmpty()) {
      List<TestStep> list = new ArrayList<>(tc.getTestSteps());
      Collections.sort(list);
      for (TestStep ts : list) {
        createCBTestStepDocumentation(ts, folder);
      }
    }
  }

  private void createCBTestStepDocumentation(TestStep ts, String parentDirName) {
    String folder = parentDirName + File.separator + ts.getPosition() + "." + ts.getName();
    createTestStepArtifacts(ts, folder);
  }

  /**
   * Copies the documentation artifacts that belong to a container entity (test plan, test case
   * group or test case). All containers carry a Test Story; test plans additionally carry a Test
   * Package and a Test Plan Summary.
   */
  private void copyContainerArtifacts(AbstractTestCase entity, String folderPath) {
    File folder = new File(folderPath);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    copyArtifact(entity.getTestStory(), TEST_STORY_FILE, folder);
    if (entity instanceof TestPlan) {
      TestPlan tp = (TestPlan) entity;
      copyArtifact(tp.getTestPackage(), TEST_PACKAGE_FILE, folder);
      copyArtifact(tp.getTestPlanSummary(), TEST_PLAN_SUMMARY_FILE, folder);
    }
  }

  private void createTestStepArtifacts(AbstractTestCase ts, String folderPath) {
    File folder = new File(folderPath);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    logger.debug("createTestStepArtifacts called for: {}, type: {}", ts.getName(),
        ts.getClass().getSimpleName());

    if (ts instanceof CFTestStep) {
      CFTestStep cfts = (CFTestStep) ts;
      // For CF tests, check if there's a TestContext with HL7v2 artifacts
      if (cfts.getTestContext() != null) {
        extractHL7V2Artifacts(cfts.getTestContext(), folder);
      }
    } else if (ts instanceof TestStep) {
      TestStep testStep = (TestStep) ts;
      copyArtifact(testStep.getTestStory(), TEST_STORY_FILE, folder);
      copyArtifact(testStep.getMessageContent(), "MessageContent.pdf", folder);
      copyArtifact(testStep.getTestDataSpecification(), "TestDataSpecification.pdf", folder);
      copyArtifact(testStep.getJurorDocument(), "JurorDocument.pdf", folder);
      // Also check for TestContext artifacts
      if (testStep.getTestContext() != null) {
        extractHL7V2Artifacts(testStep.getTestContext(), folder);
      }
    }
  }

  private static final String GET_XML = "getXml";
  private static final String TEST_STORY_FILE = "TestStory.pdf";
  private static final String TEST_PACKAGE_FILE = "TestPackage.pdf";
  private static final String TEST_PLAN_SUMMARY_FILE = "TestPlanSummary.pdf";

  private void extractHL7V2Artifacts(Object testContext, File folder) {
    try {
      Class<?> contextClass = testContext.getClass();
      String className = contextClass.getSimpleName();
      logger.debug("TestContext class: {}", className);

      if ("HL7V2TestContext".equals(className)) {
        extractArtifact(testContext, contextClass, "getConformanceProfile", GET_XML, "ConformanceProfile.xml", folder);
        extractArtifact(testContext, contextClass, "getConstraints", GET_XML, "Constraints.xml", folder);
        extractArtifact(testContext, contextClass, "getCoConstraints", GET_XML, "CoConstraints.xml", folder);
        extractArtifact(testContext, contextClass, "getVocabularyLibrary", GET_XML, "ValueSetLibrary.xml", folder);
        extractArtifact(testContext, contextClass, "getValueSetBindings", GET_XML, "ValueSetBindings.xml", folder);
        extractArtifact(testContext, contextClass, "getSlicings", GET_XML, "Slicings.xml", folder);
        extractArtifact(testContext, contextClass, "getMessage", "getContent", "MessageExample.txt", folder);
      }
    } catch (Exception e) {
      logger.debug("Error extracting HL7v2 artifacts: {}", e.getMessage());
    }
  }

  /**
   * Extracts a single artifact from the (reflectively accessed) test context and writes it to a
   * file. {@code getterName} is invoked on the context to obtain the artifact, then
   * {@code contentGetter} is invoked on that artifact to obtain the textual content to write.
   */
  private void extractArtifact(Object testContext, Class<?> contextClass, String getterName,
      String contentGetter, String fileName, File folder) {
    try {
      Object artifact = contextClass.getMethod(getterName).invoke(testContext);
      if (artifact != null) {
        Object content = artifact.getClass().getMethod(contentGetter).invoke(artifact);
        if (content != null) {
          writeStringToFile(folder, fileName, content.toString());
          logger.debug("Extracted {}", fileName);
        }
      }
    } catch (Exception e) {
      logger.debug("Could not extract {}: {}", fileName, e.getMessage());
    }
  }

  private void writeStringToFile(File folder, String fileName, String content) {
    try {
      File file = new File(folder, fileName);
      FileUtils.writeStringToFile(file, content, "UTF-8");
    } catch (Exception e) {
      logger.debug("Error writing file {}: {}", fileName, e.getMessage());
    }
  }

  private void copyArtifact(gov.nist.hit.core.domain.TestArtifact artifact, String fileName,
      File folder) {
    if (artifact == null || artifact.getPdfPath() == null) {
      return;
    }

    try {
      String filePath = artifact.getPdfPath();

      // Try to get from classpath first
      URL resourceUrl = DocumentationUtils.class.getResource("/" + filePath);
      if (resourceUrl != null) {
        File destinationFile = new File(folder, fileName);
        FileUtils.copyURLToFile(resourceUrl, destinationFile);
        return;
      }

      // Try as filesystem path
      File sourceFile = new File(filePath);
      if (sourceFile.exists()) {
        File destinationFile = new File(folder, fileName);
        FileUtils.copyFile(sourceFile, destinationFile);
      }
    } catch (Exception e) {
      // Log and continue if artifact cannot be copied
    }
  }



}
