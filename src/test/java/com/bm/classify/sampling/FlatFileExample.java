package com.bm.classify.sampling;

/**
 * Beipspiel: Wird als CSV datei rausgeschrieben.
 * 
 * @author Daniel Wiese
 * @since 15.08.2006
 */
public class FlatFileExample {

    private String name;

    private Long param1;

    private Integer param2;

    private Double param3;

    private Boolean param4;

    /**
     * Returns the name.
     * 
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the param1.
     * 
     * @return Returns the param1.
     */
    public Long getParam1() {
        return this.param1;
    }

    /**
     * Sets the param1.
     * 
     * @param param1
     *            The param1 to set.
     */
    public void setParam1(Long param1) {
        this.param1 = param1;
    }

    /**
     * Returns the param2.
     * 
     * @return Returns the param2.
     */
    public Integer getParam2() {
        return this.param2;
    }

    /**
     * Sets the param2.
     * 
     * @param param2
     *            The param2 to set.
     */
    public void setParam2(Integer param2) {
        this.param2 = param2;
    }

    /**
     * Returns the param3.
     * 
     * @return Returns the param3.
     */
    public Double getParam3() {
        return this.param3;
    }

    /**
     * Sets the param3.
     * 
     * @param param3
     *            The param3 to set.
     */
    public void setParam3(Double param3) {
        this.param3 = param3;
    }

    /**
     * Returns the param4.
     * 
     * @return Returns the param4.
     */
    public Boolean getParam4() {
        return this.param4;
    }

    /**
     * Sets the param4.
     * 
     * @param param4
     *            The param4 to set.
     */
    public void setParam4(Boolean param4) {
        this.param4 = param4;
    }

    /**
     * Der hash code.
     * 
     * @return - der hash code
     * @author Daniel Wiese
     * @since 15.08.2006
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = PRIME * result + ((this.param1 == null) ? 0 : this.param1.hashCode());
        result = PRIME * result + ((this.param2 == null) ? 0 : this.param2.hashCode());
        result = PRIME * result + ((this.param3 == null) ? 0 : this.param3.hashCode());
        result = PRIME * result + ((this.param4 == null) ? 0 : this.param4.hashCode());
        return result;
    }

    /**
     * Die equals methode.
     * 
     * @param obj -
     *            der andere
     * @return true wenn gelich.
     * @author Daniel Wiese
     * @since 15.08.2006
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlatFileExample other = (FlatFileExample) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.param1 == null) {
            if (other.param1 != null) {
                return false;
            }
        } else if (!this.param1.equals(other.param1)) {
            return false;
        }
        if (this.param2 == null) {
            if (other.param2 != null) {
                return false;
            }
        } else if (!this.param2.equals(other.param2)) {
            return false;
        }

        if (this.param3 == null) {
            if (other.param3 != null) {
                return false;
            }
        } else if (!this.param3.equals(other.param3)) {
            return false;
        }

        if (this.param4 == null) {
            if (other.param4 != null) {
                return false;
            }
        } else if (!this.param4.equals(other.param4)) {
            return false;
        }
        return true;
    }

    /**
     * To String.
     * 
     * @return back
     * @author Daniel Wiese
     * @since 26.08.2006
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(this.name).append(" : ");
        sb.append("Param1: ").append(this.param1).append(" : ");
        sb.append("Param2: ").append(this.param2).append(" : ");
        sb.append("Param3: ").append(this.param3).append(" : ");
        sb.append("Param4: ").append(this.param4);
        return sb.toString();
    }

}
