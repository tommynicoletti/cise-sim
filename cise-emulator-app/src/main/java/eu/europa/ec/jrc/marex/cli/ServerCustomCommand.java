package eu.europa.ec.jrc.marex.cli;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.ConfigOverride;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.europa.ec.jrc.marex.CiseEmulatorApplication;
import eu.europa.ec.jrc.marex.CiseEmulatorConfiguration;
import eu.europa.ec.jrc.marex.core.DropWizardCustomServerRunner;
import eu.europa.ec.jrc.marex.util.ConfigManager;
import io.dropwizard.Application;
import io.dropwizard.cli.Cli;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.util.JarLocation;
import jdk.nashorn.internal.ir.ObjectNode;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ServerCustomCommand extends Command {

    public ServerCustomCommand() {
        super("cliserver", "customized server with functional console output");
    }

    private static String TempConfigFile;
    private static final ObjectMapper DEFAULT_MAPPER;
    private static final JsonSerializer DEFAULT_SERIALIZER;
    static{
        YAMLFactory yf = new YAMLFactory();
        DEFAULT_MAPPER = new ObjectMapper();
        //configure mapper
        DEFAULT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Ignore null values when writing json.
        DEFAULT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DEFAULT_SERIALIZER= new JsonSerializer() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeObject(o);
            }
        };
    }
    @Override
    public void configure(Subparser subparser) {

        subparser.addArgument("-c", "--config")
                .dest("config")
                .type(String.class)
                .required(false)
                .help("add config path in order to send configured destination ");

        subparser.addArgument("-i", "--inputDirectory")
                .dest("inputDirectory")
                .type(String.class)
                .required(false)
                .help("add directory input path to save the received content  ");
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        final Cli cli = new Cli(new JarLocation(getClass()), bootstrap, System.out, System.err);
        ConfigManager configManager = new ConfigManager(bootstrap);
        String inputDirectory = namespace.getString("inputDirectory");
        String configfile = namespace.getString("config");

        CiseEmulatorConfiguration emulatorConfig = configManager.readExistCiseEmulatorConfiguration ("./conf/cliconfig.yml");
        if (!(configfile.isEmpty())) {
            emulatorConfig = configManager.readExistCiseEmulatorConfiguration(configfile);
        }
        if (inputDirectory != null ) {
            emulatorConfig.setInputDirectory(inputDirectory);
        }
        int av=new Double(Math.random()*999999L).intValue();
        TempConfigFile= "./tmp/config"+av+".yml";
        if (new File(TempConfigFile).exists()) (new File(TempConfigFile)).delete();

        CiseEmulatorApplication server = DropWizardCustomServerRunner.createServer(emulatorConfig,CiseEmulatorApplication.class );
        server.run(new String[]{"server", DropWizardCustomServerRunner.tmpConfigFile.toPath().toAbsolutePath().toString()});
        // mapper.createObjectNode();
        // FileOutputStream fos = new FileOutputStream(new File(fileconf));
        // yf.createGenerator(fos).writeObject(root);
        // String[] param = {"server", fileconf };
        /* TODO create a temporal configuration file serializing the resulting configuration created
        String tempFileName="emulator"+ "_" + System.currentTimeMillis();
        File tmpConfigFile = new File(
                System.getProperty("java.io.tmpdir"),tempFileName
                );
        tmpConfigFile.deleteOnExit();
        bootstrap.getObjectMapper().writeValue(tmpConfigFile);
        */
        // cli.run(param); //(bootstrap.getApplication(),param);
    }

}