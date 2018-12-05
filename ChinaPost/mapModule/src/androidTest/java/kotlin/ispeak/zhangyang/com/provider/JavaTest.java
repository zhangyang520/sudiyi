package kotlin.ispeak.zhangyang.com.provider;

import com.suntray.chinapost.map.data.bean.AdStyleEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class JavaTest {
    @Test
    public void useAppContext() throws Exception {
        System.out.println(AdStyleEnum.valueOf("预定").ordinal());
    }
}
