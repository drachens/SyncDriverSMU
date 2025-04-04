package Integration;

import com.marsol.domain.model.PLU;
import com.marsol.domain.service.backup.BackupPLU;
import org.junit.jupiter.api.Test;

import java.util.List;


public class txtToPLUTest {

    @Test
    public void txtToPLUTest() {
        BackupPLU backupPLU = new BackupPLU();
        String filepath = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\pendings\\plu_13.txt";

        List<PLU> sas = backupPLU.extractPLUs(filepath);
        for(PLU i: sas){
            System.out.println(i.toString());
        }
    }
}
