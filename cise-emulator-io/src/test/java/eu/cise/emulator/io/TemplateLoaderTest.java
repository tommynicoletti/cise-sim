package eu.cise.emulator.io;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import eu.cise.emulator.EmuConfig;
import eu.cise.emulator.exceptions.DirectoryNotFoundEx;
import eu.cise.emulator.exceptions.TemplateNotFoundEx;
import eu.cise.emulator.templates.DefaultTemplateLoader;
import eu.cise.emulator.templates.Template;
import eu.cise.emulator.templates.TemplateLoader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TemplateLoaderTest {

    private TemplateLoader templateLoader;
    private EmuConfig emuConfig;

    @Before
    public void before() {
        emuConfig = mock(EmuConfig.class);
        templateLoader = new DefaultTemplateLoader(emuConfig);

        when(emuConfig.messageTemplateDir())
            .thenReturn(getAbsPathFromResourceDir("templateDir"));
    }

    @After
    public void after() {
        reset(emuConfig);
    }

    @Test
    public void it_returns_a_list_of_template_loading_files_in_templates_dir() {
        List<Template> templateList = templateLoader.loadTemplateList();

        assertThat(templateList).isInstanceOf(List.class);
    }

    @Test
    public void it_loads_a_the_three_files_in_templates_dir() {
        List<Template> templateList = templateLoader.loadTemplateList();

        assertThat(templateList).hasSize(3);
    }

    @Test
    public void it_loads_a_file_named_COM_01_Vessel_a_xml() {
        List<Template> templateList = templateLoader.loadTemplateList();

        assertThat(templateList)
            .contains(new Template("COM_01_Vessel_a.xml", "COM_01_Vessel_a.xml"));
    }

    @Test
    public void it_returns_an_exception_when_requested_directory_doesNotExist() {
        when(emuConfig.messageTemplateDir()).thenReturn("not-existing-dir");

        assertThatExceptionOfType(DirectoryNotFoundEx.class)
            .isThrownBy(() -> templateLoader.loadTemplateList());
    }

    @Test
    public void it_returns_a_templateId() {
        Template template = templateLoader.loadTemplate("COM_01_Vessel_a.xml");

        assertThat(template.getTemplateId()).isEqualTo("COM_01_Vessel_a.xml");
    }

    @Test
    public void it_returns_a_templateName() {
        Template template = templateLoader.loadTemplate("COM_01_Vessel_a.xml");

        assertThat(template.getTemplateName()).isEqualTo("COM_01_Vessel_a.xml");
    }

    @Test
    public void it_returns_a_templateContent() throws IOException {
        Template template = templateLoader.loadTemplate("COM_01_Vessel_a.xml");

        assertThat(template.getTemplateContent())
            .isEqualTo(readResource("templateDir/COM_01_Vessel_a.xml"));
    }

    @Test
    public void it_throw_an_exception_when_the_file_missing() {
        assertThatExceptionOfType(TemplateNotFoundEx.class)
            .isThrownBy(() -> templateLoader.loadTemplate("not_existing_file.xml"))
            .withMessageContaining("not_existing_file.xml");
    }

    @Test
    public void it_throw_an_exception_when_the_templateId_is_null() {
        assertThatExceptionOfType(TemplateNotFoundEx.class)
            .isThrownBy(() -> templateLoader.loadTemplate(null))
            .withMessageContaining("null");
    }

    private String readResource(String resourceName) throws IOException {
        Path path = Paths.get(getResourceURI(resourceName));

        return new String(Files.readAllBytes(path), UTF_8);
    }

    private String getAbsPathFromResourceDir(String resourceDir) {
        return new File(getResourceURI(resourceDir)).getAbsolutePath();
    }

    private URI getResourceURI(String resourceDir) {
        try {
            return this.getClass().getClassLoader().getResource(resourceDir).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}