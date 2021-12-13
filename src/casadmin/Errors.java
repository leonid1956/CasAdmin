/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package casadmin;

/**
 *
 * @author lpylypch
 */
public class Errors {
    private static int iLastError;
    private static String sLastError;
    public static void setLastError(int p_iLastError, String p_sLastError)
    {
        iLastError = p_iLastError;
        sLastError = p_sLastError;
    }

    public static int getLastErrorInt() {
        return iLastError;
    }

    public static String getLastErrorString() {
        return sLastError;
    }
}
