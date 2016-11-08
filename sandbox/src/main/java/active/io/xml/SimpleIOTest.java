package active.io.xml;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SimpleIOTest {

    @Test
    public void comparePartyTest() throws Exception {
        compareTest("otherparty.xml");
    }

    @Test
    public void compareFightTest() throws Exception {
        compareTest("fight.xml");
    }


    public void compareTest(String file) throws Exception {
        File inFile = new File(SimpleIOTest.class.getResource("/"+file).toURI());
        File  outFile = File.createTempFile("dndtest-"+file, ".xml");

        try (InputStream inStream = new FileInputStream(inFile);
             OutputStream outStream = new FileOutputStream(outFile)) {
            XMLFormat.DndData data = XMLFormat.importData().apply(inStream);
            System.out.println(data);
            XMLFormat.exportData(data).accept(outStream);
        }

        String in = clean(new String(Files.readAllBytes(inFile.toPath())));
        String out = clean(new String(Files.readAllBytes(outFile.toPath())));

        System.out.println(in);
        System.out.println(out);

        assertThat(out).isXmlEqualTo(in);
//        assertThat(out).isEqualTo(in);
    }

    private String clean(String string){
        return string
                .replaceAll("\\<\\!\\-\\-.+?\\-\\-\\>", "")
                .replaceAll("\\<(.+)\\>\\s*\\<\\/\\1\\>", "<$1/>")
                .replaceAll("\\<(.+?)\\s*\\/\\>", "<$1/>")
                .replaceAll("\\s+", " ")
                ;
    }

}
