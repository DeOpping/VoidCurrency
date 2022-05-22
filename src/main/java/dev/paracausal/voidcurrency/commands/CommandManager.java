package dev.paracausal.voidcurrency.commands;

import dev.paracausal.voidcurrency.Core;
import dev.paracausal.voidcurrency.commands.balance.Balance;
import dev.paracausal.voidcurrency.commands.voidcurrency.VoidCurrency;
import dev.paracausal.voidcurrency.commands.voidcurrency.modules.*;
import dev.paracausal.voidcurrency.utilities.configurations.ConfigManager;

public class CommandManager {

    Core core;
    ConfigManager configYml;

    public CommandManager(Core core) {
        this.core = core;
        this.configYml = core.getConfigYml();
    }

    public void initialize() {
        this.vcInfo = new VCInfo(core);
        this.vcHelp = new VCHelp(core);
        this.vcReload = new VCReload(core);

        this.vcSet = new VCSet(core);
        this.vcAdd = new VCAdd(core);
        this.vcRemove = new VCRemove(core);

        this.voidCurrency = new VoidCurrency(core);
        core.getServer().getPluginCommand("voidcurrency").setExecutor(voidCurrency);

        this.balance = new Balance(core);
        if (configYml.getConfig().getBoolean("modules.balance")) {
            core.getServer().getPluginCommand("balance").setExecutor(balance);
        }
    }

    private VoidCurrency voidCurrency;
    public VoidCurrency getVoidCurrency() { return voidCurrency; }

    private VCInfo vcInfo;
    public VCInfo getVcInfo() { return vcInfo; }

    private VCHelp vcHelp;
    public VCHelp getVcHelp() { return vcHelp; }

    private VCReload vcReload;
    public VCReload getVcReload() { return vcReload; }

    private VCSet vcSet;
    public VCSet getVcSet() { return vcSet; }

    private VCAdd vcAdd;
    public VCAdd getVcAdd() { return vcAdd; }

    private VCRemove vcRemove;
    public VCRemove getVcRemove() { return vcRemove; }

    private Balance balance;
    public Balance getBalance() { return balance; }

}
