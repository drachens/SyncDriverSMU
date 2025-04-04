import com.marsol.domain.model.PLU;
import com.marsol.domain.service.backup.BackupPLU;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class BackupPluTest {

    @Test
    public void testExtractPlusFromFile() {
        String filepath = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto SMU\\SyncDriverSMU\\src\\test\\resources\\plu_13.txt";
        List<PLU> plus = BackupPLU.extractPLUs(filepath);

        assertThat(plus).isNotEmpty();
        assertThat(plus.get(0).getLFCode()).isGreaterThan(0);
    }

    @Test
    public void testThrowExceptionForMissingFile() {
        String nonExistingPath = "test_that_does_not_exist.txt";

        assertThatThrownBy(() -> BackupPLU.extractPLUs(nonExistingPath))
        .isInstanceOf(RuntimeException.class);
    }
}
