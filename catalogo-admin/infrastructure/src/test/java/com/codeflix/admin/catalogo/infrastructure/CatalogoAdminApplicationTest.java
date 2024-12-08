package com.codeflix.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CatalogoAdminApplicationTest {

    @Test
    public void testMain() {
        Assertions.assertNotNull(new CatalogoAdminApplication());
        CatalogoAdminApplication.main(new String[]{});
    }
}
