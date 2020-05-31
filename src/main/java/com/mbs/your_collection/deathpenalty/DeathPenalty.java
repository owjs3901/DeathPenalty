package com.mbs.your_collection.deathpenalty;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class DeathPenalty extends JavaPlugin implements Listener {

	private double defaultRemove=0.2;
	private double defaultDrop=0.3;
	private boolean keepInv=true;

	//drop,remove,playerDrop,playerRemove
	private HashMap<String,State> map=new HashMap<>();
	private Metrics metrics;

	@Override
	public void onEnable() {
		metrics =new Metrics(this,7212 );


		Bukkit.getPluginManager().registerEvents(this,this);

		loadConfig();
	}

	private void loadConfig(){
		if(getConfig().contains("keepInv"))
			keepInv=getConfig().getBoolean("keepInv");
		else
			getConfig().set("keepInv",keepInv);


		if(getConfig().contains("defaultRemove"))
			defaultRemove=getConfig().getDouble("defaultRemove");
		else
			getConfig().set("defaultRemove",defaultRemove);

		if(getConfig().contains("defaultDrop"))
			defaultRemove=getConfig().getDouble("defaultDrop");
		else
			getConfig().set("defaultDrop",defaultRemove);

		for (World world : Bukkit.getWorlds()) {




			double remove = defaultRemove;
			double drop = defaultDrop;
			double removePlayer = defaultRemove;
			double dropPlayer = defaultDrop;
			final String path=world.getName()+".";
			/**
			 * 일반적인 사망
			 */
			if(getConfig().contains(path+"drop"))
				drop=getConfig().getDouble(path+"drop");
			else getConfig().set(path+"drop",drop);

			if(getConfig().contains(path+"remove"))
				remove=getConfig().getDouble(path+"remove");
			else getConfig().set(path+"remove",remove);
			/**
			 * 플레이어에 의한 사망
			 */
			if(getConfig().contains(path+"drop_player"))
				drop=getConfig().getDouble(path+"drop_player");
			else getConfig().set(path+"drop_player",drop);

			if(getConfig().contains(path+"remove_player"))
				remove=getConfig().getDouble(path+"remove_player");
			else getConfig().set(path+"remove_player",remove);
			
			map.put(world.getName(),new State(drop,remove,dropPlayer,removePlayer));
			switch(world.getName()){
				case "world":case "world_nether":case "world_the_end":
					double finalDrop = drop;
					metrics.addCustomChart(new Metrics.SimplePie(world.getName()+"_drop", () -> String.valueOf(finalDrop)));
					double finalRemove = remove;
					metrics.addCustomChart(new Metrics.SimplePie(world.getName()+"_remove", () -> String.valueOf(finalRemove)));

					double finalDropPlayer = dropPlayer;
					metrics.addCustomChart(new Metrics.SimplePie(world.getName()+"_drop_player", () -> String.valueOf(finalDropPlayer)));
					double finalRemovePlayer = removePlayer;
					metrics.addCustomChart(new Metrics.SimplePie(world.getName()+"_remove_player", () -> String.valueOf(finalRemovePlayer)));

					break;
			}


			if(keepInv)
				world.setGameRuleValue("keepInventory","true");
//				for (String gameRule : world.getGameRules()) {
//					System.out.println(gameRule);
//				}
//				world.setGameRule(GameRule.KEEP_INVENTORY,true);
		}
		saveConfig();
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
//		loadConfig();
	}
	private Random r=new Random();

	@EventHandler
	public void death(PlayerDeathEvent e){
		List<ItemStack> list=new ArrayList<>();
		ItemStack inOffHand=e.getEntity().getInventory().getItemInOffHand();
		/**
		 * 왼손일때 없어지는 오류 수정
		 */
		if(inOffHand!=null)
			list.add(inOffHand);

		for (ItemStack armorContent : e.getEntity().getInventory().getArmorContents()) {
			if(armorContent!=null)
				list.add(armorContent);
		}
		for (ItemStack storageContent : e.getEntity().getInventory().getStorageContents()) {
			if(storageContent!=null)
				list.add(storageContent);
		}
		String name=e.getEntity().getWorld().getName();
		double remove=defaultRemove;
		double drop=defaultDrop;
		if(map.containsKey(name)){
			if(e.getEntity().getKiller()!=null){
				drop=map.get(name).getDropFromPlayer();
				remove=map.get(name).getRemoveFromPlayer();
			}
			else{
				drop=map.get(name).getDrop();
				remove=map.get(name).getRemove();
			}

		}
		int drop1= (int) (drop*list.size());
		int remove1= (int) (remove*list.size());

		for (int i = 0; i < drop1; i++) {
			if(list.size()>0)
				e.getDrops().add(list.remove(r.nextInt(list.size())));
		}
		for (int i = 0; i < remove1; i++) {
			if(list.size()>0)
				list.remove(r.nextInt(list.size()));
		}
		e.getEntity().getInventory().clear();
		for (int i = 0; i < list.size(); i++) {
			e.getEntity().getInventory().setItem(i,list.get(i));
		}
	}


	@Override
	public void onDisable() {
	}
}
