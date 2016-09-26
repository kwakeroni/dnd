package io;

import active.io.xml.XMLInput;
import active.io.xml.XMLOutput;
import active.model.creature.Party;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SimpleIOTest {

    @Test
    public void compareTest() throws Exception {
        File inFile = new File(SimpleIOTest.class.getResource("/otherparty.xml").toURI());
        Party party = XMLInput.readParties(inFile).iterator().next();
        System.out.println(party);
        File  outFile = File.createTempFile("dndtest", ".xml");
        XMLOutput.writeToFile(party, outFile);

        String in = clean(new String(Files.readAllBytes(inFile.toPath())));
        String out = clean(new String(Files.readAllBytes(outFile.toPath())));

        System.out.println(in);
        System.out.println(out);

        assertThat(out).isXmlEqualTo(in);
        assertThat(out).isEqualTo(in);
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
