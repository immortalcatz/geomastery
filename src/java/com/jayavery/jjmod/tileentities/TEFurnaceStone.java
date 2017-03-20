package com.jayavery.jjmod.tileentities;

import com.jayavery.jjmod.blocks.BlockNew;
import com.jayavery.jjmod.init.ModBlocks;
import com.jayavery.jjmod.init.ModItems;
import com.jayavery.jjmod.init.ModRecipes;
import com.jayavery.jjmod.tileentities.TEFurnaceStone.EnumPartStone;
import com.jayavery.jjmod.utilities.IMultipart;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** TileEntity for stone furnace blocks. */
public class TEFurnaceStone extends TEFurnaceAbstract<EnumPartStone> {

    public TEFurnaceStone() {

        super(ModRecipes.STONE, 6);
    }
    
    @Override
    public EnumPartStone partByOrdinal(int ordinal) {
        
        return EnumPartStone.values()[ordinal];
    }

    /** Enum defining parts of the whole stone furnace structure. */
    public enum EnumPartStone implements IMultipart {

        BL("bl"), BM("bm"), BR("br"), TL("tl"), TM("tm"), TR("tr");

        private final String name;

        private EnumPartStone(String name) {

            this.name = name;
        }

        @Override
        public String getName() {

            return this.name;
        }
        
        @Override
        public ItemStack getDrop() {
            
            if (this == BL) {
                
                return new ItemStack(ModItems.furnaceStone);
                
            } else {
                
                return ItemStack.EMPTY;
            }
        }
        
        @Override
        public BlockPos getMaster(BlockPos pos, EnumFacing facing) {
            
            switch (this) {

                case BM:
                    return pos.offset(facing.rotateY().getOpposite());
                case BR:
                    return pos.offset(facing.rotateY().getOpposite(), 2);
                case TL:
                    return pos.down();
                case TM: 
                    return pos.offset(facing.rotateY().getOpposite()).down();
                case TR: 
                    return pos.offset(facing.rotateY().getOpposite(), 2).down();
                case BL:
                default: 
                    return pos;
            }
        }
        
        @Override
        public boolean shouldBreak(World world, BlockPos pos,
                EnumFacing facing) {
            
            boolean broken = false;
            Block block = ModBlocks.furnaceStone;
            
            switch (this) {
                
                case BM: {

                    broken = world.getBlockState(pos.offset(facing.rotateY()))
                            .getBlock() != block;
                    break;
                }

                case BR: {

                    broken = world.getBlockState(pos.up()).getBlock() != block;
                    break;
                }

                case TR: {

                    broken = world.getBlockState(pos.offset(facing.rotateY()
                            .getOpposite())).getBlock() != block;
                    break;
                }

                case TM: {

                    broken = world.getBlockState(pos.offset(facing.rotateY()
                            .getOpposite())).getBlock() != block;
                    break;
                }

                case TL: {

                    broken = world.getBlockState(pos.down()).getBlock()
                            != block;
                    break;
                }

                case BL: {

                    broken = world.getBlockState(pos.offset(facing.rotateY()))
                            .getBlock() != block;
                    break;
                }
            }
            
            return broken;
        }
        
        @Override
        public AxisAlignedBB getBoundingBox(EnumFacing facing) {
            
            switch (this) {

                case TR: 
                case TM: 
                case TL: 
                    return BlockNew.TWELVE;
                    
                case BR: 
                case BM:             
                case BL:
                default: 
                    return Block.FULL_BLOCK_AABB;
            }
        }
        
        @Override
        public AxisAlignedBB getCollisionBox(EnumFacing facing) {
            
            return this.getBoundingBox(facing);
        }
        
        @Override
        public boolean buildStructure(World world, BlockPos pos,
                EnumFacing facing) {
            
            if (this == BM) {
                
                BlockPos posBM = pos;
                BlockPos posBL = posBM.offset(facing.rotateY().getOpposite());
                BlockPos posBR = posBM.offset(facing.rotateY());
                BlockPos posTL = posBL.up();
                BlockPos posTM = posBM.up();
                BlockPos posTR = posBR.up();

                // Check replaceable
                IBlockState stateBL = world.getBlockState(posBL);
                Block blockBL = stateBL.getBlock();
                boolean replaceableBL = blockBL.isReplaceable(world, posBL);

                IBlockState stateBM = world.getBlockState(posBM);
                Block blockBM = stateBM.getBlock();
                boolean replaceableBM = blockBM.isReplaceable(world, posBM);

                IBlockState stateBR = world.getBlockState(posBR);
                Block blockBR = stateBR.getBlock();
                boolean replaceableBR = blockBR.isReplaceable(world, posBR);

                IBlockState stateTL = world.getBlockState(posTL);
                Block blockTL = stateTL.getBlock();
                boolean replaceableTL = blockTL.isReplaceable(world, posTL);

                IBlockState stateTM = world.getBlockState(posTM);
                Block blockTM = stateTM.getBlock();
                boolean replaceableTM = blockTM.isReplaceable(world, posTM);

                IBlockState stateTR = world.getBlockState(posTR);
                Block blockTR = stateTR.getBlock();
                boolean replaceableTR = blockTR.isReplaceable(world, posTR);

                if (replaceableBL && replaceableBM && replaceableBR &&
                        replaceableTL && replaceableTM && replaceableTR) {

                    // Place all
                    IBlockState placeState = ModBlocks
                            .furnaceStone.getDefaultState();
    
                    world.setBlockState(posBM, placeState);
                    world.setBlockState(posBR, placeState);
                    world.setBlockState(posTR, placeState);
                    world.setBlockState(posTM, placeState);
                    world.setBlockState(posTL, placeState);
                    world.setBlockState(posBL, placeState);
    
                    // Set up tileentities with data
                    ((TEFurnaceStone) world.getTileEntity(posBL))
                            .setState(facing, BL);
                    ((TEFurnaceStone) world.getTileEntity(posBM))
                            .setState(facing, BM);
                    ((TEFurnaceStone) world.getTileEntity(posBR))
                            .setState(facing, BR);
                    ((TEFurnaceStone) world.getTileEntity(posTR))
                            .setState(facing, TR);
                    ((TEFurnaceStone) world.getTileEntity(posTM))
                            .setState(facing, TM);
                    ((TEFurnaceStone) world.getTileEntity(posTL))
                            .setState(facing, TL);
                    
                    return true;
                }
            }
            
            return false;
        }
    }
}