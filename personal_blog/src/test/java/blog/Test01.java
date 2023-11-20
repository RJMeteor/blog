package blog;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import java.util.regex.Pattern;

public class Test01 {

    @Test
    public void tr(){
        Pattern regex = Pattern.compile("error|web|re");
        boolean replacedText = regex.matcher("/bt/er").find();
        System.out.println(replacedText);

    }
}
