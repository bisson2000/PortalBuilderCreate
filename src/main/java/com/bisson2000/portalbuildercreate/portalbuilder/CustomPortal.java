package com.bisson2000.portalbuildercreate.portalbuilder;

import java.awt.*;

public class CustomPortal {

    final public @JsonDeserializerWithOptions.FieldRequired String frameBlock;
    final public @JsonDeserializerWithOptions.FieldRequired String igniter;
    final public @JsonDeserializerWithOptions.FieldRequired String fromDim;
    final public @JsonDeserializerWithOptions.FieldRequired String toDim;
    final public @JsonDeserializerWithOptions.FieldRequired Integer r;
    final public @JsonDeserializerWithOptions.FieldRequired Integer g;
    final public @JsonDeserializerWithOptions.FieldRequired Integer b;
    public Boolean onlyIgniteInDims;
    public Boolean isFlatPortal;
    public Boolean forceSize;
    public Integer forcedSizeWidth;
    public Integer forcedSizeHeight;
    public Boolean showCredits;

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
            Boolean showCredits) {
        this(frameBlock, igniter, fromDim, toDim, color.getRed(), color.getGreen(), color.getBlue(), onlyIgnitInDims, isFlatPortal, forceSize, forcedSizeWidth, forcedSizeHeight, showCredits);
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
            Boolean showCredits
    ) {
        // Required fields
        this.frameBlock = frameBlock;

        this.igniter = igniter;

        this.fromDim = fromDim;

        this.toDim = toDim;

        this.r = r;
        this.g = g;
        this.b = b;

        // Optional fields
        this.onlyIgniteInDims = onlyIgnitInDims == null ? true : onlyIgnitInDims;

        this.isFlatPortal = isFlatPortal == null ? false : isFlatPortal;

        this.forceSize = forceSize == null ? false : forceSize;
        this.forcedSizeWidth = forcedSizeWidth == null ? 0 : forcedSizeWidth;
        this.forcedSizeHeight = forcedSizeHeight == null ? 0 :forcedSizeHeight;

        this.showCredits = showCredits == null ? false : showCredits;
    }

    public void cleanupFields() {
        this.onlyIgniteInDims = onlyIgniteInDims == null ? true : onlyIgniteInDims;

        this.isFlatPortal = isFlatPortal == null ? false : isFlatPortal;

        this.forceSize = forceSize == null ? false : forceSize;
        this.forcedSizeWidth = forcedSizeWidth == null ? 0 : forcedSizeWidth;
        this.forcedSizeHeight = forcedSizeHeight == null ? 0 :forcedSizeHeight;

        this.showCredits = showCredits == null ? false : showCredits;

    }

}