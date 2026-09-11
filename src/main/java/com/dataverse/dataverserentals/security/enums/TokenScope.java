package com.dataverse.dataverserentals.security.enums;

public enum TokenScope {
    MFA_SETUP,
    MFA_VERIFY,
    FULL_ACCESS;

    public String asAuthority() {
        return switch (this) {
            case MFA_SETUP -> "SCOPE_MFA_SETUP";
            case MFA_VERIFY -> "SCOPE_MFA_VERIFY";
            case FULL_ACCESS -> "SCOPE_FULL_ACCESS";
        };
    }
}
