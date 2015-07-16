import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuotian on 15/7/16.
 */
public class Test01 {
    public static void main(String[] args){
        Scanner s=new Scanner(System.in);
        String str=s.nextLine();
        final String REGEX="^[a-z;]+$";
        final String REGEX1="^[0-9;]+$";
        final String REGEX2="^[a-z0-9;]+$";
        Matcher matcher = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE).matcher(str);
        if(matcher.find()){
            System.out.println("纯英");
        }else{
            matcher=Pattern.compile(REGEX1, Pattern.CASE_INSENSITIVE).matcher(str);
            if(matcher.find()){
                System.out.println("不能为纯数字");
            }else{
                matcher=Pattern.compile(REGEX2, Pattern.CASE_INSENSITIVE).matcher(str);
                if(matcher.find()){
                    System.out.println("英数混合");
                }else{
                    System.out.println("不能有特殊字符");
                }
            }
        }
    }
}
