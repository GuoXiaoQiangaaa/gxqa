import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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


    @Test
    public void TestReg() {


        String str = "PO 1231231231; 12\n PO  1231231232 \n PO:1231231233;123 \n PO：1231231234 -0 \n PO#1231231235 \n PO1234561234";
//        String REGEX = "^PO(\\s{0,2}|(:)|(：)|(#))\\d{10}$";
        String REGEX = "PO(\\s{1,2}|(:)|(：)|(#))\\d{10}";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(str); // 获取 matcher 对象
        int count = 0;

        while (m.find()) {
            count++;
            System.out.println("Match number " + count);
            System.out.println("start(): " + m.start());
            System.out.println("end(): " + m.end());
            String findStr = m.group().substring(m.group().length() - 10, m.group().length());
            System.out.println("group(): " + findStr);
        }

    }
}

