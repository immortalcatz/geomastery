package com.jayavery.jjmod.blocks;

import java.util.List;
import com.jayavery.jjmod.init.ModBlocks;
import com.jayavery.jjmod.utilities.BlockMaterial;
import com.jayavery.jjmod.utilities.IBuildingBlock;
import com.jayavery.jjmod.utilities.ToolType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/** Stairs block with up to a single connection, no corners. */
public class BlockStairsStraight extends BlockNew {
    
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumConnection> CONNECTION =
            PropertyEnum.<EnumConnection>create("connection",
            EnumConnection.class);
    
    public BlockStairsStraight(String name, float hardness,
            ToolType harvestTool) {
        
        super(BlockMaterial.WOOD_FURNITURE, name,
                CreativeTabs.BUILDING_BLOCKS, hardness, harvestTool);
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        
        return this.hasFoundation(world, pos) &&
                this.hasValidConnections(world, pos);
    }
    
    /** @return Whether this block has a valid
     * foundation at the given position. */
    private boolean hasFoundation(World world, BlockPos pos) {
        
        Block block = world.getBlockState(pos.down()).getBlock();
        
        boolean natural = false;
        boolean built = false;
        
        natural = ModBlocks.LIGHT.contains(block) ||
                ModBlocks.HEAVY.contains(block);
        
        if (block instanceof IBuildingBlock) {
            
            IBuildingBlock builtBlock = (IBuildingBlock) block;
            built = builtBlock.isLight() || builtBlock.isHeavy();
        }
        
        return natural || built;
    }
    
    /** Checks whether this block has less than two adjacent of the same block.
     * @return Whether this block is valid at the given position. */
    private boolean hasValidConnections(World world, BlockPos pos) {
        
        int count = 0;

        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            
            Block block = world.getBlockState(pos.offset(direction)).getBlock();
            
            if (block == this) {
                
                count++;
                
                Block nextBlock = world.getBlockState(pos
                        .offset(direction, 2)).getBlock();
                
                if (nextBlock == this) {
                    
                    return false;
                }
            }
        }
        
        if (count > 1) {
            
            return false;
        }
        
        return true;
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos,
            EnumFacing side, float x, float y, float z,
            int meta, EntityLivingBase placer) {
        
        return this.getDefaultState().withProperty(FACING,
                placer.getHorizontalFacing());
    }
    
    @Override
    public IBlockState getActualState(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        EnumFacing facing = state.getValue(FACING);
        EnumFacing left = facing.rotateYCCW();
        EnumFacing right = facing.rotateY();
        
        if (world.getBlockState(pos.offset(right)).getBlock() == this) {
            
            state = state.withProperty(CONNECTION, EnumConnection.RIGHT);
            
        } else if (world.getBlockState(pos.offset(left)).getBlock() == this) {
            
            state = state.withProperty(CONNECTION, EnumConnection.LEFT);
            
        } else {
            
            state = state.withProperty(CONNECTION, EnumConnection.NONE);
        }

        return state;
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World world,
            BlockPos pos, AxisAlignedBB entityBox,
            List<AxisAlignedBB> list, Entity entity, boolean unused) {
                
        int facing = (state.getValue(FACING).getHorizontalIndex() + 1) % 4;
        
        for (AxisAlignedBB box : STAIRS_STRAIGHT[facing]) {
            
            addCollisionBoxToList(pos, entityBox, list, box);
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        
        return new BlockStateContainer(this,
                new IProperty[] {FACING, CONNECTION});
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        return this.getDefaultState().withProperty(FACING, facing);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        
        return state.getValue(FACING).getHorizontalIndex();
    }

    /** Enum defining connection type of this block. */
    public enum EnumConnection implements IStringSerializable {
        
        LEFT("left"), RIGHT("right"), NONE("none");
        
        private String name;
        
        private EnumConnection(String name) {
            
            this.name = name;
        }

        @Override
        public String getName() {

            return this.name;
        }
    }
}