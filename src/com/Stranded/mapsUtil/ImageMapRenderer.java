package com.Stranded.mapsUtil;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class ImageMapRenderer extends MapRenderer {

    private BufferedImage image;
    private boolean updated = false;

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {

        MapRenderer renderer = mapCanvas.getMapView().getRenderers().get(0);
        if (renderer instanceof ImageMapRenderer) {
            ImageMapRenderer mapRenderer = (ImageMapRenderer) renderer;
            if (!mapRenderer.updated) {
                mapCanvas.drawImage(0, 0, image);
                mapRenderer.updated = true;
            }
        }
    }

    ImageMapRenderer(BufferedImage image) {
        this.image = image;
    }

    public void update() {
        updated = false;
    }

    public void update(BufferedImage image) {
        this.image = image;
        updated = false;
    }
}
