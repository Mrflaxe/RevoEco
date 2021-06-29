package ru.mrflaxe.revoeco.commands.sub;

import ru.soknight.lib.command.enhanced.help.command.EnhancedHelpExecutor;
import ru.soknight.lib.command.response.CommandResponseType;
import ru.soknight.lib.configuration.Messages;

public class SubcommandHelp extends EnhancedHelpExecutor {

    public SubcommandHelp(Messages messages) {
        super(messages);
        
        super.setHeaderFrom("command.help.header");
        super.setFooterFrom("command.help.footer");
        
        super.factory()
                .helpLineFormatFrom("command.help.body")
                .permissionFormat("reco.command.%s")
                .descriptionPathFormat("command.help.description.%s")
                .argumentPathFormat("command.help.arguments.%s")
                
                .newLine()
                    .command("reco help")
                    .descriptionFrom("help")
                    .permission("help")
                    .add()
                    
                .newLine()
                    .command("balance")
                    .descriptionFrom("balance")
                    .argumentFrom("player-opt")
                    .permission("balance")
                    .add()
        
                .newLine()
                    .command("baltop")
                    .descriptionFrom("baltop")
                    .argumentFrom("page")
                    .permission("balancetop")
                    .add()
                    
                .newLine()
                    .command("pay")
                    .descriptionFrom("pay")
                    .argumentFrom("player-req")
                    .permission("pay")
                    .add()
                    
                .newLine()
                    .command("history")
                    .descriptionFrom("history")
                    .argumentFrom("player-opt")
                    .argumentFrom("page")
                    .permission("history")
                    .add()
                    
                .newLine()
                    .command("reco give")
                    .descriptionFrom("give")
                    .argumentFrom("player-req")
                    .argumentFrom("sum-req")
                    .permission("give")
                    .add()
                    
                .newLine()
                    .command("reco take")
                    .descriptionFrom("take")
                    .argumentFrom("player-req")
                    .argumentFrom("sum-req")
                    .permission("take")
                    .add()
                    
                .newLine()
                    .command("reco nullify")
                    .descriptionFrom("nullify")
                    .argumentFrom("player-req")
                    .permission("nullify")
                    .add()
                    
                .newLine()
                    .command("reco globalnullify")
                    .descriptionFrom("globalnullify")
                    .permission("globalnullify")
                    .add()
                    
                .newLine()
                    .command("reco configrefresh")
                    .descriptionFrom("configrefresh")
                    .permission("configrefresh")
                    .add();
                    
        super.completeMessage();
        
        super.setPermission("reco.command.help");
        super.setResponseMessageByKey(CommandResponseType.NO_PERMISSIONS, "error.no-permissions");
    }
}
