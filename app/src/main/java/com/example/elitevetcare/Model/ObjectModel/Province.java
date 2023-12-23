package com.example.elitevetcare.Model.ObjectModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Province {
    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private int code;

    @SerializedName("division_type")
    private String divisionType;

    @SerializedName("codename")
    private String codename;

    @SerializedName("phone_code")
    private int phoneCode;

    @SerializedName("districts")
    private List<District> districts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDivisionType() {
        return divisionType;
    }

    public void setDivisionType(String divisionType) {
        this.divisionType = divisionType;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public class District {
        @SerializedName("name")
        private String name;

        @SerializedName("code")
        private int code;

        @SerializedName("codename")
        private String codename;

        @SerializedName("division_type")
        private String divisionType;

        @SerializedName("short_codename")
        private String shortCodename;

        @SerializedName("wards")
        private List<Ward> wards;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getCodename() {
            return codename;
        }

        public void setCodename(String codename) {
            this.codename = codename;
        }

        public String getDivisionType() {
            return divisionType;
        }

        public void setDivisionType(String divisionType) {
            this.divisionType = divisionType;
        }

        public String getShortCodename() {
            return shortCodename;
        }

        public void setShortCodename(String shortCodename) {
            this.shortCodename = shortCodename;
        }

        public List<Ward> getWards() {
            return wards;
        }

        public void setWards(List<Ward> wards) {
            this.wards = wards;
        }

        public class Ward {
            @SerializedName("name")
            private String name;

            @SerializedName("code")
            private int code;

            @SerializedName("codename")
            private String codename;

            @SerializedName("division_type")
            private String divisionType;

            @SerializedName("short_codename")
            private String shortCodename;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getCodename() {
                return codename;
            }

            public void setCodename(String codename) {
                this.codename = codename;
            }

            public String getDivisionType() {
                return divisionType;
            }

            public void setDivisionType(String divisionType) {
                this.divisionType = divisionType;
            }

            public String getShortCodename() {
                return shortCodename;
            }

            public void setShortCodename(String shortCodename) {
                this.shortCodename = shortCodename;
            }
        }
    }
}
