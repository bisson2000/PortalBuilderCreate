package com.bisson2000.portalbuildercreate.portalbuilder;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

public record CustomPortal(
        String __frameBlock_comment,
        @JsonDeserializerWithOptions.FieldRequired String frameBlock,
        String __igniter_comment,
        @JsonDeserializerWithOptions.FieldRequired String igniter,
        String __fromDim_comment,
        @JsonDeserializerWithOptions.FieldRequired String fromDim,
        String __toDim_comment,
        @JsonDeserializerWithOptions.FieldRequired String toDim,
        String __rgb_comment,
        @JsonDeserializerWithOptions.FieldRequired int r,
        @JsonDeserializerWithOptions.FieldRequired int g,
        @JsonDeserializerWithOptions.FieldRequired int b,
        String __onlyIgnitInFromDim_comment,
        boolean onlyIgnitInDims,
        String __isFlatPortal_comment,
        boolean isFlatPortal,
        String __forceSize_comment,
        boolean forceSize,
        int forcedSizeWidth,
        int forcedSizeHeight,
        String __activationFlag_comment,
        boolean activationFlag
) {
    final static String frameBlock_comment = "@Required. The block used to create the portal frame. Must be in format modid:block_name. E.g.: minecraft:diamond_block";
    final static String igniter_comment = "@Required. The item/block/fluid used to ignite the Portal. Must be in format modid:block_name. E.g.: minecraft:fire";
    final static String fromDim_comment = "@Required. The dimension from which the portal starts. Must be in format modid:dim_name. E.g.: minecraft:overworld";
    final static String toDim_comment = "@Required. The dimension from which the portal ends. Must be in format modid:dim_name. E.g.: minecraft:the_nether";
    final static String rgb_comment = "@Required. The color of the portal. Red, Green and Blue values must be between 0 and 255";
    final static String onlyIgnitInFromDim_comment = "@Optional. True by default. True if a portal can only be ignited in one of its targeted dim. " +
            "For example, if the value is set to false, then a portal that links minecraft:the_end and minecraft:the_nether can be activated in minecraft:overworld";
    final static String isFlatPortal_comment = "@Optional. False by default. False means the portal is vertical, like the nether portal. True means the portal is horizontal, like the end portal";
    final static String forceSize_comment = "@Optional. False by default. If the portal is required to have a specific size and match the width and height specified below";
    final static String activationFlag_comment = "@Optional. True by default. Allows you to activate and deactivate portals without removing the entry. Used for this example and debugging";


    public CustomPortal(String frameBlock, String igniter, String fromDim, String toDim, Color color) {
        this(frameBlock,
                igniter,
                fromDim,
                toDim,
                color.getRed(), color.getGreen(), color.getBlue()
        );
    }

    public CustomPortal(String frameBlock, String igniter, String fromDim, String toDim, int r, int g, int b) {
        this(frameBlock_comment,
                frameBlock,
                igniter_comment,
                igniter,
                fromDim_comment,
                fromDim,
                toDim_comment,
                toDim,
                rgb_comment,
                r, g, b,
                onlyIgnitInFromDim_comment,
                true,
                isFlatPortal_comment,
                false,
                forceSize_comment,
                false,
                0, 0,
                activationFlag_comment,
                true
        );
    }

}