package com.bendude56.dungeonman.item;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

import com.bendude56.dungeonman.GameInstance;
import com.bendude56.dungeonman.entity.EntityPlayer;
import com.bendude56.dungeonman.gfx.ImageUtil;
import com.bendude56.dungeonman.ui.GameFrame;

/**
 * @author Benjamin C. Thomas
 */

public abstract class ItemPotion extends ItemConsumable implements ItemIdentifiable {
    public static final Image potionSprite = ImageUtil.loadImage("/entity/item/potion.png");
    
    public static final String[] adjectives = new String[] { "Bubbly", "Thick", "Oily", "Foggy", "Hazy", "Fizzy", "Murky" };
    public static final String[] colorNames = new String[] { "Black", "White", "Green", "Purple", "Blue", "Red", "Cyan", "Yellow" };
    public static final Color[] colors = new Color[] { Color.black, Color.white, Color.green, Color.magenta, Color.blue,
            Color.red, Color.cyan, Color.yellow };
    
    private Image sprite;
    private String name;
    private String unidentifiedName;
    private int identifyLevel;
    
    public ItemPotion(String name, String unidentifiedName, Color c, int id, int rarity, int identifyLevel) {
        super(id, rarity, 1);
        
        this.name = name;
        this.unidentifiedName = unidentifiedName;
        this.sprite = ImageUtil.colorPotion(potionSprite, c);
        this.identifyLevel = identifyLevel;
    }
    
    @Override
    public Image getDrawImage(ItemStack stack) {
        return sprite;
    }
    
    @Override
    public String getItemName(ItemStack stack) {
        return GameInstance.getActiveInstance().isItemIdentified(this) ? name : unidentifiedName;
    }
    
    @Override
    public int getWeight(ItemStack stack) {
        return 200;
    }
    
    @Override
    public void tryIdentify(EntityPlayer e) {
        if (GameInstance.getActiveInstance().isItemIdentified(this)) {
            GameFrame.activeFrame.logMessage("You have already identified that!");
        } else if (e.getStats().getIntelligence() >= identifyLevel) {
            GameInstance.getActiveInstance().setItemIdentified(this, true);
            GameFrame.activeFrame.logMessage("You successfully identify the item!");
        } else {
            GameFrame.activeFrame.logMessage("You fail to identify the item!");
        }
    }
    
    /**
     * Generates a random new potion, with a random appearance and effect on the
     * player.
     * 
     * @param id The unique identifier that the potion should be given.
     * @param difficulty The difficulty in which the game is running.
     * @param random A randomizer that can be used to randomize the new potion's
     *            properties.
     * 
     * @return The {@link ItemPotion} which has been generated.
     */
    public static ItemPotion generateNewPotion(int id, int difficulty, Random random) {
        String adjective = adjectives[random.nextInt(adjectives.length)];
        int color = random.nextInt(colors.length);
        int type = random.nextInt(2);
        int identifyLevel = random.nextInt(10);
        
        if (type == 0) {
            // Healing potion
            return new ItemHealingPotion(adjective + " " + colorNames[color] + " Potion", colors[color], id, 10,
                    random.nextInt(96) + 5, identifyLevel);
        } else if (type == 1) {
            // Damage potion
            return new ItemDamagePotion(adjective + " " + colorNames[color] + " Potion", colors[color], id, 10,
                    random.nextInt(96) + 5, identifyLevel);
        } else {
            // Should never happen
            return null;
        }
    }
}
