package com.bisson2000.portalbuildercreate.portalbuilder;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

public class CustomPortal {

    final public String __frameBlock_comment;
    final public @JsonDeserializerWithOptions.FieldRequired String frameBlock;
    final public String __igniter_comment;
    final public @JsonDeserializerWithOptions.FieldRequired String igniter;
    final public String __fromDim_comment;
    final public @JsonDeserializerWithOptions.FieldRequired String fromDim;
    final public String __toDim_comment;
    final public @JsonDeserializerWithOptions.FieldRequired String toDim;
    final public String __rgb_comment;
    final public @JsonDeserializerWithOptions.FieldRequired Integer r;
    final public @JsonDeserializerWithOptions.FieldRequired Integer g;
    final public @JsonDeserializerWithOptions.FieldRequired Integer b;
    public String __onlyIgnitInFromDim_comment;
    public Boolean onlyIgnitInDims;
    public String __isFlatPortal_comment;
    public Boolean isFlatPortal;
    public String __forceSize_comment;
    public Boolean forceSize;
    public Integer forcedSizeWidth;
    public Integer forcedSizeHeight;
    public String __activationFlag_comment;
    public Boolean activationFlag;

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

    public CustomPortal(String frameBlock, String igniter, String fromDim, String toDim, Integer r, Integer g, Integer b) {
        this(frameBlock,
                igniter,
                fromDim,
                toDim,
                r, g, b,
                true,
                false,
                false,
                0,
                0,
                true
        );
    }

    public CustomPortal(
            String frameBlock,
            String igniter,
            String fromDim,
            String toDim,
            Color color,
            Boolean onlyIgnitInDims,
            Boolean isFlatPortal,
            Boolean forceSize,
            Integer forcedSizeWidth,
            Integer forcedSizeHeight,
            Boolean activationFlag) {
        this(frameBlock, igniter, fromDim, toDim, color.getRed(), color.getGreen(), color.getBlue(), onlyIgnitInDims, isFlatPortal, forceSize, forcedSizeWidth, forcedSizeHeight, activationFlag);
    }

    public CustomPortal(
            String frameBlock,
            String igniter,
            String fromDim,
            String toDim,
            Integer r,
            Integer g,
            Integer b,
            Boolean onlyIgnitInDims,
            Boolean isFlatPortal,
            Boolean forceSize,
            Integer forcedSizeWidth,
            Integer forcedSizeHeight,
            Boolean activationFlag
    ) {
        // Required fields
        this.__frameBlock_comment = frameBlock_comment;
        this.frameBlock = frameBlock;

        this.__igniter_comment = igniter_comment;
        this.igniter = igniter;

        this.__fromDim_comment = fromDim_comment;
        this.fromDim = fromDim;

        this.__toDim_comment = toDim_comment;
        this.toDim = toDim;

        this.__rgb_comment = rgb_comment;
        this.r = r;
        this.g = g;
        this.b = b;

        // Optional fields
        this.__onlyIgnitInFromDim_comment = onlyIgnitInFromDim_comment;
        this.onlyIgnitInDims = onlyIgnitInDims == null ? true : onlyIgnitInDims;

        this.__isFlatPortal_comment = isFlatPortal_comment;
        this.isFlatPortal = isFlatPortal == null ? false : isFlatPortal;

        this.__forceSize_comment = forceSize_comment;
        this.forceSize = forceSize == null ? false : forceSize;
        this.forcedSizeWidth = forcedSizeWidth == null ? 0 : forcedSizeWidth;
        this.forcedSizeHeight = forcedSizeHeight == null ? 0 :forcedSizeHeight;

        this.__activationFlag_comment = activationFlag_comment;
        this.activationFlag = activationFlag == null ? true : activationFlag;
    }

    public void cleanupFields() {
        this.__onlyIgnitInFromDim_comment = onlyIgnitInFromDim_comment;
        this.onlyIgnitInDims = onlyIgnitInDims == null ? true : onlyIgnitInDims;

        this.__isFlatPortal_comment = isFlatPortal_comment;
        this.isFlatPortal = isFlatPortal == null ? false : isFlatPortal;

        this.__forceSize_comment = forceSize_comment;
        this.forceSize = forceSize == null ? false : forceSize;
        this.forcedSizeWidth = forcedSizeWidth == null ? 0 : forcedSizeWidth;
        this.forcedSizeHeight = forcedSizeHeight == null ? 0 :forcedSizeHeight;

        this.__activationFlag_comment = activationFlag_comment;
        this.activationFlag = activationFlag == null ? true : activationFlag;

    }

}