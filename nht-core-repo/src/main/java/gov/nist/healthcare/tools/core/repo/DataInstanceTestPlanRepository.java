/**
 * This software was developed at the National Institute of Standards and Technology by employees
 * of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */

package gov.nist.healthcare.tools.core.repo;

import java.util.List;

import gov.nist.healthcare.tools.core.models.DataInstanceTestPlan;
import gov.nist.healthcare.tools.core.models.IsolatedTestCase;
import gov.nist.healthcare.tools.core.models.IsolatedTestCaseGroup;
import gov.nist.healthcare.tools.core.models.IsolatedTestPlan;
import gov.nist.healthcare.tools.core.models.IsolatedTestStep;
import gov.nist.healthcare.tools.core.models.Stage;
import gov.nist.healthcare.tools.core.models.SoapTestCase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DataInstanceTestPlanRepository extends JpaRepository<DataInstanceTestPlan, Long> {
	
	@Query("select tp from DataInstanceTestPlan tp where tp.stage = :stage")
	public List<DataInstanceTestPlan> findAllByStage(@Param("stage") Stage stage);

}
