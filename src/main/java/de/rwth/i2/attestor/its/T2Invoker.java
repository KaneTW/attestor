package de.rwth.i2.attestor.its;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class T2Invoker {
    private File t2Executable;

    public T2Invoker(String filePath) {
        File exe = new File(filePath);
        if (!exe.exists()) {
            throw new IllegalArgumentException("T2 executable not found at " + filePath);
        }

        t2Executable = exe;
    }

    private Process invokeT2(Path workingDir, String... args) throws IOException {
        List<String> command = new ArrayList<>();
        command.add("mono");
        command.add(t2Executable.getAbsolutePath());
        command.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(command);

//        pb.redirectInput(ProcessBuilder.Redirect.INHERIT)
//                .redirectOutput(ProcessBuilder.Redirect.PIPE)
//                .redirectError(ProcessBuilder.Redirect.PIPE);

        pb.directory(workingDir.toFile());

        return pb.start();
    }

    public T2Result checkTermination(ITS its) throws IOException {
        Path dir = Files.createTempDirectory("attestor_its_");
        Path file = Files.createTempFile(dir,"its_", ".t2");

        String certPath = file.toString() + ".cert";

        Files.write(file, its.toString().getBytes());

        Process t2 = invokeT2(dir, "--termination", "--export_cert=" + certPath, "--export_cert_hints", file.toString());

        try {
            t2.waitFor(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            return new T2Result (T2Status.TIMEOUT, dir, null);
        }

        if (t2.exitValue() != 0) {
            return new T2Result (T2Status.ERROR, dir, null);
        }

        Document doc = null;

        Scanner stream = new Scanner(t2.getInputStream());
        while (stream.hasNextLine()) {
            String line = stream.nextLine();
            if (line.startsWith("Termination proof succeeded")) {
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    doc = builder.parse(certPath);
                } catch (SAXException ex) {
                    throw new RuntimeException(ex);
                } catch (ParserConfigurationException ex) {
                    throw new RuntimeException(ex);
                }
                return new T2Result (T2Status.TERMINATING, dir, doc);
            }

            if (line.startsWith("Nontermination proof succeeded")) {
                return new T2Result (T2Status.NONTERMINATING, dir, doc);
            }

            if (line.startsWith("Termination/nontermination proof failed")) {
                return new T2Result (T2Status.MAYBE, dir, doc);
            }
        }




//        Files.walk(dir)
//             .sorted(Comparator.reverseOrder())
//             .map(Path::toFile)
//             .forEach(File::delete);

        return new T2Result(T2Status.ERROR, dir, null);
    }
}
