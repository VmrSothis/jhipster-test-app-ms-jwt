package com.sothis.tech.domain;

import static com.sothis.tech.domain.MachineDocumentationTestSamples.*;
import static com.sothis.tech.domain.MachineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineDocumentationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MachineDocumentation.class);
        MachineDocumentation machineDocumentation1 = getMachineDocumentationSample1();
        MachineDocumentation machineDocumentation2 = new MachineDocumentation();
        assertThat(machineDocumentation1).isNotEqualTo(machineDocumentation2);

        machineDocumentation2.setId(machineDocumentation1.getId());
        assertThat(machineDocumentation1).isEqualTo(machineDocumentation2);

        machineDocumentation2 = getMachineDocumentationSample2();
        assertThat(machineDocumentation1).isNotEqualTo(machineDocumentation2);
    }

    @Test
    void machineTest() throws Exception {
        MachineDocumentation machineDocumentation = getMachineDocumentationRandomSampleGenerator();
        Machine machineBack = getMachineRandomSampleGenerator();

        machineDocumentation.setMachine(machineBack);
        assertThat(machineDocumentation.getMachine()).isEqualTo(machineBack);

        machineDocumentation.machine(null);
        assertThat(machineDocumentation.getMachine()).isNull();
    }
}
