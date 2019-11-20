package gov.nist.hit.core.service;

import java.io.InputStream;
import java.util.List;

import gov.nist.hit.core.domain.ManualValidationResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.service.exception.ValidationReportException;

public interface TestStepValidationReportService {

  TestStepValidationReport save(TestStepValidationReport report);

  void delete(Long id);

  TestStepValidationReport findOne(Long id);

  void delete(List<TestStepValidationReport> resutls);

  void save(List<TestStepValidationReport> resutl);

  TestStepValidationReport findOneByTestStepAndUser(Long testStepId, Long userId);

  List<TestStepValidationReport> findAllByTestStepAndUser(Long testStepId, Long userId);

  List<TestStepValidationReport> findAllByUser(Long userId);

  TestStepValidationReport findOneByIdAndUser(Long reportId, Long userId);

  TestStepValidationReport saveXmlReport(Long testStepId, Long userId, String xmlReport);

  TestStepValidationReport saveHtmlReport(Long testStepId, Long userId, String htmlReport);



  /**
   * Here we merge the message validation report with the manual inputs
   * 
   * @param report
   * @return
   * @throws ValidationReportException
   */
  String generateXmlTestStepValidationReport(String xmlMessageValidationReport,
      TestStepValidationReport report, TestStep testStep) throws ValidationReportException;

  String generateHtml(String xml) throws ValidationReportException;

  String generateXhtml(String xml) throws ValidationReportException;

  InputStream generatePdf(String xml) throws ValidationReportException;
  
  InputStream generatePdf2(String xml) throws ValidationReportException;

  String generateManualXml(ManualValidationResult validationResult);

  /**
   * We update the xml test step validation report based on the user's inputs
   * 
   * @param report
   * @return
   * @throws ValidationReportException
   */
  String updateXmlTestValidationReportElement(TestStepValidationReport report)
      throws ValidationReportException;

  List<TestStepValidationReport> findAllByTestStep(Long testStepId);



}
