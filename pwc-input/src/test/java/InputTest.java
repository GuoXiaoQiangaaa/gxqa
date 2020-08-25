import org.dom4j.DocumentException;
import org.junit.Test;
import org.ofdrw.core.basicStructure.ofd.DocBody;
import org.ofdrw.core.basicStructure.ofd.OFD;
import org.ofdrw.core.basicStructure.ofd.docInfo.CT_DocInfo;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.reader.OFDReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputTest {

    @Test
    public void TestOFD() throws Exception {
//        ZipUtil.unzip("/Users/zk/Downloads/pwc/033021800111_96422040.ofd");

        ZipUtil.unzip("/Users/zk/Downloads/pwc/033021800111_96422040.ofd", "/Users/zk/Downloads/pwc", true);
//        Path path = Paths.get("/Users/zk/Downloads/pwc/033021800111_96422040.ofd");
//        OFDReader ofdReader = new OFDReader(path);
//        OFDDir ofdDir = ofdReader.getOFDDir();
//        OFD ofd = ofdDir.getOfd();
//        DocBody docBody = ofd.getDocBody();
//        CT_DocInfo docInfo = docBody.getDocInfo();
//        System.out.printf(docInfo.getStringValue());
    }
}
