package com.jayavery.jjmod.utilities;

import net.minecraft.util.ResourceLocation;

/** Enum defining stages of temperature and associated icons. */
public enum TempStage {
    
    COLD("cold"), COOL("cool"), OK("ok"), WARM("warm"), HOT("hot");
    
    /** Texture of this icon. */
    private ResourceLocation res;
    
    private TempStage(String name) {
        
        this.res = new ResourceLocation("jjmod:textures/gui/temp_" +
                name + ".png");
    }
    
    /** @return The stage the given temperature belongs to. */
    public static TempStage fromTemp(float temp) {
        
        if (temp <= 0) {
            
            return COLD;
            
        } else if (temp <= 1.5) {
            
            return COOL;
            
        } else if (temp <=3.5) {
            
            return OK;
            
        } else if (temp <= 5) {
            
            return WARM;
            
        } else {
            
            return HOT;
        }
    }
    
    /** @return The ResourceLocation of the icon for this Stage. */
    public ResourceLocation toResourceLocation() {
        
        return this.res;
    }
}
