package org.springframework.samples.petclinic;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.VerificationReports;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {PetClinicApplication.class})
@Provider("PetClinic")
@PactFolder("pacts")
@VerificationReports(value = {"console", "markdown"}, reportDir = "/home/jeremy/dev/contract/spring-petclinic-rest/pacts/reports")
public class PactVerificationTest {

    public static final Logger LOG = LoggerFactory.getLogger(PactVerificationTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    PetTypeRepository petTypeRepository;

    @BeforeEach
    void setup(PactVerificationContext context) {
        // Tells PACT where to find the running server
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State(value = "test state", action = StateChangeAction.SETUP)
    void testState(){
        LOG.info("Types={}", petTypeRepository.findAll());
    }


}
