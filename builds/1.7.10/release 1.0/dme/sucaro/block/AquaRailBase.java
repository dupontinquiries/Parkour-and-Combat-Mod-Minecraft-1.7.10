package dme.sucaro.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class AquaRailBase extends Block
{
    protected final boolean field_150053_a;
    private static final String __OBFID = "CL_00000195";

    public static final boolean func_150049_b_(World p_150049_0_, int p_150049_1_, int p_150049_2_, int p_150049_3_)
    {
        return func_150051_a(p_150049_0_.getBlock(p_150049_1_, p_150049_2_, p_150049_3_));
    }

    public static final boolean func_150051_a(Block p_150051_0_)
    {
        return p_150051_0_ instanceof AquaRailBase;
    }

    protected AquaRailBase(boolean p_i45389_1_)
    {
        super(Material.circuits);
        this.field_150053_a = p_i45389_1_;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    /**
     * Returns true if the block is power related rail.
     */
    public boolean isPowered()
    {
        return this.field_150053_a;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World p_149731_1_, int p_149731_2_, int p_149731_3_, int p_149731_4_, Vec3 p_149731_5_, Vec3 p_149731_6_)
    {
        this.setBlockBoundsBasedOnState(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_);
        return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);

        if (l >= 2 && l <= 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return renderType;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return true; //return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        if (!p_149726_1_.isRemote)
        {
            this.func_150052_a(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_, true);

            if (this.field_150053_a)
            {
                this.onNeighborBlockChange(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_, this);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!p_149695_1_.isRemote)
        {
            int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
            int i1 = l;

            if (this.field_150053_a)
            {
                i1 = l & 7;
            }

            boolean flag = false;

            if (!World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_))
            {
                flag = true;
            }

            if (i1 == 2 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_ + 1, p_149695_3_, p_149695_4_))
            {
                flag = true;
            }

            if (i1 == 3 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_ - 1, p_149695_3_, p_149695_4_))
            {
                flag = true;
            }

            if (i1 == 4 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_ - 1))
            {
                flag = true;
            }

            if (i1 == 5 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_ + 1))
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
                p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            }
            else
            {
                this.func_150048_a(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, l, i1, p_149695_5_);
            }
        }
    }

    protected void func_150048_a(World p_150048_1_, int p_150048_2_, int p_150048_3_, int p_150048_4_, int p_150048_5_, int p_150048_6_, Block p_150048_7_) {}

    protected void func_150052_a(World p_150052_1_, int p_150052_2_, int p_150052_3_, int p_150052_4_, boolean p_150052_5_)
    {
        if (!p_150052_1_.isRemote)
        {
            (new AquaRailBase.RailAqua(p_150052_1_, p_150052_2_, p_150052_3_, p_150052_4_)).func_150655_a(p_150052_1_.isBlockIndirectlyGettingPowered(p_150052_2_, p_150052_3_, p_150052_4_), p_150052_5_);
        }
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    public int getMobilityFlag()
    {
        return 0;
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        int i1 = p_149749_6_;

        if (this.field_150053_a)
        {
            i1 = p_149749_6_ & 7;
        }

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);

        if (i1 == 2 || i1 == 3 || i1 == 4 || i1 == 5)
        {
            p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ + 1, p_149749_4_, p_149749_5_);
        }

        if (this.field_150053_a)
        {
            p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
            p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ - 1, p_149749_4_, p_149749_5_);
        }
    }

    /* ======================================== FORGE START =====================================*/
    /**
     * Return true if the rail can make corners.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make corners.
     */
    public boolean isFlexibleRail(IBlockAccess world, int y, int x, int z)
    {
        return !isPowered();
    }

    /**
     * Returns true if the rail can make up and down slopes.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make slopes.
     */
    public boolean canMakeSlopes(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    /**
     * Return the rail's metadata (without the power bit if the rail uses one).
     * Can be used to make the cart think the rail something other than it is,
     * for example when making diamond junctions or switches.
     * The cart parameter will often be null unless it it called from EntityMinecart.
     * 
     * Valid rail metadata is defined as follows:
     * 0x0: flat track going North-South
     * 0x1: flat track going West-East
     * 0x2: track ascending to the East
     * 0x3: track ascending to the West
     * 0x4: track ascending to the North
     * 0x5: track ascending to the South
     * 0x6: WestNorth corner (connecting East and South)
     * 0x7: EastNorth corner (connecting West and South)
     * 0x8: EastSouth corner (connecting West and North)
     * 0x9: WestSouth corner (connecting East and North)
     * 
     * @param world The world.
     * @param cart The cart asking for the metadata, null if it is not called by EntityMinecart.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The metadata.
     */
    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if(isPowered())
        {
            meta = meta & 7;
        }
        return meta;
    }

    /**
     * Returns the max speed of the rail at the specified position.
     * @param world The world.
     * @param cart The cart on the rail, may be null.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The max speed of the current rail.
     */
    public float getRailMaxSpeed(World world, EntityMinecart cart, int y, int x, int z)
    {
        return 4.4f; //.4f
    }

    /**
     * This function is called by any minecart that passes over this rail.
     * It is called once per update tick that the minecart is on the rail.
     * @param world The world.
     * @param cart The cart on the rail.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     */
    public void onMinecartPass(World world, EntityMinecart cart, int y, int x, int z)
    {
    }    
    
    /**
     * Forge: Moved render type to a field and a setter.
     * This allows for a mod to change the render type
     * for vanilla rails, and any mod rails that extend
     * this class.
     */
    private int renderType = 9;
    
    public void setRenderType(int value)
    {
        renderType = value;
    }
    /* ======================================== FORGE END =====================================*/

    public class RailAqua
    {
        private World field_150660_b;
        private int field_150661_c;
        private int field_150658_d;
        private int field_150659_e;
        private final boolean field_150656_f;
        private List field_150657_g = new ArrayList();
        private static final String __OBFID = "CL_00000196";
        private final boolean canMakeSlopes;

        public RailAqua(World p_i45388_2_, int p_i45388_3_, int p_i45388_4_, int p_i45388_5_)
        {
            this.field_150660_b = p_i45388_2_;
            this.field_150661_c = p_i45388_3_;
            this.field_150658_d = p_i45388_4_;
            this.field_150659_e = p_i45388_5_;
            AquaRailBase block = (AquaRailBase)p_i45388_2_.getBlock(p_i45388_3_, p_i45388_4_, p_i45388_5_);
            int l = block.getBasicRailMetadata(p_i45388_2_, null, p_i45388_3_, p_i45388_4_, p_i45388_5_);
            this.field_150656_f = !block.isFlexibleRail(p_i45388_2_, p_i45388_3_, p_i45388_4_, p_i45388_5_);
            canMakeSlopes = block.canMakeSlopes(p_i45388_2_, p_i45388_3_, p_i45388_4_, p_i45388_5_);
            this.func_150648_a(l);
        }

        private void func_150648_a(int p_150648_1_)
        {
            this.field_150657_g.clear();

            if (p_150648_1_ == 0)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 1)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
            }
            else if (p_150648_1_ == 2)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d + 1, this.field_150659_e));
            }
            else if (p_150648_1_ == 3)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d + 1, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
            }
            else if (p_150648_1_ == 4)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d + 1, this.field_150659_e - 1));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 5)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d + 1, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 6)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 7)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 8)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
            }
            else if (p_150648_1_ == 9)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
            }
        }

        private void func_150651_b()
        {
            for (int i = 0; i < this.field_150657_g.size(); ++i)
            {
                AquaRailBase.RailAqua rail = this.func_150654_a((ChunkPosition)this.field_150657_g.get(i));

                if (rail != null && rail.func_150653_a(this))
                {
                    this.field_150657_g.set(i, new ChunkPosition(rail.field_150661_c, rail.field_150658_d, rail.field_150659_e));
                }
                else
                {
                    this.field_150657_g.remove(i--);
                }
            }
        }

        private boolean func_150646_a(int p_150646_1_, int p_150646_2_, int p_150646_3_)
        {
            return AquaRailBase.func_150049_b_(this.field_150660_b, p_150646_1_, p_150646_2_, p_150646_3_) ? true : (AquaRailBase.func_150049_b_(this.field_150660_b, p_150646_1_, p_150646_2_ + 1, p_150646_3_) ? true : AquaRailBase.func_150049_b_(this.field_150660_b, p_150646_1_, p_150646_2_ - 1, p_150646_3_));
        }

        private AquaRailBase.RailAqua func_150654_a(ChunkPosition p_150654_1_)
        {
            return AquaRailBase.func_150049_b_(this.field_150660_b, p_150654_1_.chunkPosX, p_150654_1_.chunkPosY, p_150654_1_.chunkPosZ) ? AquaRailBase.this.new RailAqua(this.field_150660_b, p_150654_1_.chunkPosX, p_150654_1_.chunkPosY, p_150654_1_.chunkPosZ) : (AquaRailBase.func_150049_b_(this.field_150660_b, p_150654_1_.chunkPosX, p_150654_1_.chunkPosY + 1, p_150654_1_.chunkPosZ) ? AquaRailBase.this.new RailAqua(this.field_150660_b, p_150654_1_.chunkPosX, p_150654_1_.chunkPosY + 1, p_150654_1_.chunkPosZ) : (AquaRailBase.func_150049_b_(this.field_150660_b, p_150654_1_.chunkPosX, p_150654_1_.chunkPosY - 1, p_150654_1_.chunkPosZ) ? AquaRailBase.this.new RailAqua(this.field_150660_b, p_150654_1_.chunkPosX, p_150654_1_.chunkPosY - 1, p_150654_1_.chunkPosZ) : null));
        }

        private boolean func_150653_a(AquaRailBase.RailAqua p_150653_1_)
        {
            for (int i = 0; i < this.field_150657_g.size(); ++i)
            {
                ChunkPosition chunkposition = (ChunkPosition)this.field_150657_g.get(i);

                if (chunkposition.chunkPosX == p_150653_1_.field_150661_c && chunkposition.chunkPosZ == p_150653_1_.field_150659_e)
                {
                    return true;
                }
            }

            return false;
        }

        private boolean func_150652_b(int p_150652_1_, int p_150652_2_, int p_150652_3_)
        {
            for (int l = 0; l < this.field_150657_g.size(); ++l)
            {
                ChunkPosition chunkposition = (ChunkPosition)this.field_150657_g.get(l);

                if (chunkposition.chunkPosX == p_150652_1_ && chunkposition.chunkPosZ == p_150652_3_)
                {
                    return true;
                }
            }

            return false;
        }

        protected int func_150650_a()
        {
            int i = 0;

            if (this.func_150646_a(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1))
            {
                ++i;
            }

            if (this.func_150646_a(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1))
            {
                ++i;
            }

            if (this.func_150646_a(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e))
            {
                ++i;
            }

            if (this.func_150646_a(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e))
            {
                ++i;
            }

            return i;
        }

        private boolean func_150649_b(AquaRailBase.RailAqua p_150649_1_)
        {
            return this.func_150653_a(p_150649_1_) ? true : (this.field_150657_g.size() == 2 ? false : (this.field_150657_g.isEmpty() ? true : true));
        }

        private void func_150645_c(AquaRailBase.RailAqua p_150645_1_)
        {
            this.field_150657_g.add(new ChunkPosition(p_150645_1_.field_150661_c, p_150645_1_.field_150658_d, p_150645_1_.field_150659_e));
            boolean flag = this.func_150652_b(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1);
            boolean flag1 = this.func_150652_b(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1);
            boolean flag2 = this.func_150652_b(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e);
            boolean flag3 = this.func_150652_b(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e);
            byte b0 = -1;

            if (flag || flag1)
            {
                b0 = 0;
            }

            if (flag2 || flag3)
            {
                b0 = 1;
            }

            if (!this.field_150656_f)
            {
                if (flag1 && flag3 && !flag && !flag2)
                {
                    b0 = 6;
                }

                if (flag1 && flag2 && !flag && !flag3)
                {
                    b0 = 7;
                }

                if (flag && flag2 && !flag1 && !flag3)
                {
                    b0 = 8;
                }

                if (flag && flag3 && !flag1 && !flag2)
                {
                    b0 = 9;
                }
            }

            if (b0 == 0 && canMakeSlopes)
            {
                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e - 1))
                {
                    b0 = 4;
                }

                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e + 1))
                {
                    b0 = 5;
                }
            }

            if (b0 == 1 && canMakeSlopes)
            {
                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c + 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    b0 = 2;
                }

                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c - 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    b0 = 3;
                }
            }

            if (b0 < 0)
            {
                b0 = 0;
            }

            int i = b0;

            if (this.field_150656_f)
            {
                i = this.field_150660_b.getBlockMetadata(this.field_150661_c, this.field_150658_d, this.field_150659_e) & 8 | b0;
            }

            this.field_150660_b.setBlockMetadataWithNotify(this.field_150661_c, this.field_150658_d, this.field_150659_e, i, 3);
        }

        private boolean func_150647_c(int p_150647_1_, int p_150647_2_, int p_150647_3_)
        {
            AquaRailBase.RailAqua rail = this.func_150654_a(new ChunkPosition(p_150647_1_, p_150647_2_, p_150647_3_));

            if (rail == null)
            {
                return false;
            }
            else
            {
                rail.func_150651_b();
                return rail.func_150649_b(this);
            }
        }

        public void func_150655_a(boolean p_150655_1_, boolean p_150655_2_)
        {
            boolean flag2 = this.func_150647_c(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1);
            boolean flag3 = this.func_150647_c(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1);
            boolean flag4 = this.func_150647_c(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e);
            boolean flag5 = this.func_150647_c(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e);
            byte b0 = -1;

            if ((flag2 || flag3) && !flag4 && !flag5)
            {
                b0 = 0;
            }

            if ((flag4 || flag5) && !flag2 && !flag3)
            {
                b0 = 1;
            }

            if (!this.field_150656_f)
            {
                if (flag3 && flag5 && !flag2 && !flag4)
                {
                    b0 = 6;
                }

                if (flag3 && flag4 && !flag2 && !flag5)
                {
                    b0 = 7;
                }

                if (flag2 && flag4 && !flag3 && !flag5)
                {
                    b0 = 8;
                }

                if (flag2 && flag5 && !flag3 && !flag4)
                {
                    b0 = 9;
                }
            }

            if (b0 == -1)
            {
                if (flag2 || flag3)
                {
                    b0 = 0;
                }

                if (flag4 || flag5)
                {
                    b0 = 1;
                }

                if (!this.field_150656_f)
                {
                    if (p_150655_1_)
                    {
                        if (flag3 && flag5)
                        {
                            b0 = 6;
                        }

                        if (flag4 && flag3)
                        {
                            b0 = 7;
                        }

                        if (flag5 && flag2)
                        {
                            b0 = 9;
                        }

                        if (flag2 && flag4)
                        {
                            b0 = 8;
                        }
                    }
                    else
                    {
                        if (flag2 && flag4)
                        {
                            b0 = 8;
                        }

                        if (flag5 && flag2)
                        {
                            b0 = 9;
                        }

                        if (flag4 && flag3)
                        {
                            b0 = 7;
                        }

                        if (flag3 && flag5)
                        {
                            b0 = 6;
                        }
                    }
                }
            }

            if (b0 == 0 && canMakeSlopes)
            {
                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e - 1))
                {
                    b0 = 4;
                }

                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e + 1))
                {
                    b0 = 5;
                }
            }

            if (b0 == 1 && canMakeSlopes)
            {
                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c + 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    b0 = 2;
                }

                if (AquaRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c - 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    b0 = 3;
                }
            }

            if (b0 < 0)
            {
                b0 = 0;
            }

            this.func_150648_a(b0);
            int i = b0;

            if (this.field_150656_f)
            {
                i = this.field_150660_b.getBlockMetadata(this.field_150661_c, this.field_150658_d, this.field_150659_e) & 8 | b0;
            }

            if (p_150655_2_ || this.field_150660_b.getBlockMetadata(this.field_150661_c, this.field_150658_d, this.field_150659_e) != i)
            {
                this.field_150660_b.setBlockMetadataWithNotify(this.field_150661_c, this.field_150658_d, this.field_150659_e, i, 3);

                for (int j = 0; j < this.field_150657_g.size(); ++j)
                {
                    AquaRailBase.RailAqua rail = this.func_150654_a((ChunkPosition)this.field_150657_g.get(j));

                    if (rail != null)
                    {
                        rail.func_150651_b();

                        if (rail.func_150649_b(this))
                        {
                            rail.func_150645_c(this);
                        }
                    }
                }
            }
        }
    }
}