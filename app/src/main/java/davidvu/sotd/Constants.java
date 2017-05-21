package davidvu.sotd;

/**
 * Created by Michael Frank on 12.05.2017.
 */

public class Constants {

    private static final String ROOT_URL = "http://192.168.0.19/sotd/v1/";//10.0.105.7 oder 192.168.0.19
    public static final String URL_SUGGEST = ROOT_URL+"suggestSkill.php";
    public static final String URL_SLIST = ROOT_URL+"listSkills.php?sort=";
    public static final String URL_SIMG = ROOT_URL+"getSkillImage.php?skillname=";
    public static final String URL_SKILL = ROOT_URL+"getSkill.php?skillname=";
    public static final String URL_RATE = ROOT_URL+"rateSkill.php";
    public static final String URL_KATEGORIE = ROOT_URL+"getKategorie.php?skillname=";

    public static final String SHARED_RATE = "Rate";
    public static final String SHARED_MYLIST = "Merkliste";
    public static final String SHARED_MYLEARNT = "LearntList";
}
