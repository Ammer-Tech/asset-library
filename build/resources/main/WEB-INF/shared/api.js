function Network(document) {
  this.id = document.getElementsByName("id")[0].value;
  this.networkId = document.getElementsByName("networkId")[0].value;
  this.networkName = document.getElementsByName("networkName")[0].value;
  this.visibleName = document.getElementsByName("visibleName")[0].value;
  this.iconUrl = document.getElementsByName("iconUrl")[0].value;
  this.explorerTransactions = document.getElementsByName("explorerTransactions")[0].value;
  this.explorerAddress = document.getElementsByName("explorerAddress")[0].value;
  this.compatiblePubKeys = document.getElementsByName("compatiblePubKeys")[0].value;
  this.visible = document.getElementsByName("visible")[0].checked;
}

function BaseAsset(document){
    this.displayFactor = document.getElementsByName("displayFactor")[0].value;
    this.decimalSeparator = document.getElementsByName("displayFactor")[0].value;
    this.symbol = document.getElementsByName("displayFactor")[0].value;
    this.assetType = document.getElementsByName("displayFactor")[0].value;
    this.visibleName = document.getElementsByName("displayFactor")[0].value;
    this.visible = document.getElementsByName("displayFactor")[0].value;
    this.description = document.getElementsByName("displayFactor")[0].value;
    this.feeEnabled = document.getElementsByName("displayFactor")[0].value;
    this.feeUnits = document.getElementsByName("displayFactor")[0].value;
    this.genericLogoUrl = document.getElementsByName("displayFactor")[0].value;
    this.androidLogoUrl = document.getElementsByName("displayFactor")[0].value;
    this.iOSLogoUrl = document.getElementsByName("displayFactor")[0].value;
}

function SmartAsset(document){
    this.displayFactor = document.getElementsByName("displayFactor")[0].value;
    this.decimalSeparator = document.getElementsByName("displayFactor")[0].value;
    this.symbol = document.getElementsByName("displayFactor")[0].value;
    this.assetType = document.getElementsByName("displayFactor")[0].value;
    this.visibleName = document.getElementsByName("displayFactor")[0].value;
    this.visible = document.getElementsByName("displayFactor")[0].value;
    this.description = document.getElementsByName("displayFactor")[0].value;
    this.feeEnabled = document.getElementsByName("displayFactor")[0].value;
    this.feeUnits = document.getElementsByName("displayFactor")[0].value;
    this.genericLogoUrl = document.getElementsByName("displayFactor")[0].value;
    this.androidLogoUrl = document.getElementsByName("displayFactor")[0].value;
    this.iOSLogoUrl = document.getElementsByName("displayFactor")[0].value;
}

function MediaAsset(document){
    this.displayFactor = document.getElementsByName("displayFactor")[0].value;
    this.decimalSeparator = document.getElementsByName("displayFactor")[0].value;
    this.symbol = document.getElementsByName("displayFactor")[0].value;
    this.assetType = document.getElementsByName("displayFactor")[0].value;
    this.visibleName = document.getElementsByName("displayFactor")[0].value;
    this.visible = document.getElementsByName("displayFactor")[0].value;
    this.description = document.getElementsByName("displayFactor")[0].value;
    this.feeEnabled = document.getElementsByName("displayFactor")[0].value;
    this.feeUnits = document.getElementsByName("displayFactor")[0].value;
    this.genericLogoUrl = document.getElementsByName("displayFactor")[0].value;
    this.androidLogoUrl = document.getElementsByName("displayFactor")[0].value;
    this.iOSLogoUrl = document.getElementsByName("displayFactor")[0].value;
}



async function submitCreateNetwork()
{
    const network = new Network(document);
    const options = {method: 'POST',body: JSON.stringify(network),headers: { 'Content-Type':'application/json' }}
    var res = await fetch('/createNetwork', options);
    if(res.status !== 200){
        alert('The server returned an error - could not save the network!');
        return;
    }
    var json = await res.json()
    alert('Successfully saved the new network!')
}

async function submitCreateAsset()
{

    //continued.
    //if ERC20 then ignore the sub-tokens.
    //if ERC721/ERC1155 then squash and over-ride each one of them with the "new" values, and create
    //the ones that are missing.
    var res = await fetch();
    //Check for 200 and then get the result and pain the canvas.
    //If not 200 then show error dialog.
}

async function getAssetByIdAndType()
{
    //Get a specific asset.
    var res = await fetch();
}

async function getNetworkById()
{
    //Get a specific asset.
    var res = await fetch();
}


async function listNetworks()
{
    const options = {method: 'GET'}
    var res = await fetch('/api/public/networks', options);
    if(res.status !== 200){
        alert('Could not fetch the list of networks!');
        return;
    }
    var json = await res.json()
    alert(json)
}

async function listAssets()
{
    const options = {method: 'GET'}
    var res = await fetch('/api/public/networks/' + networkId + '/assets/smart-assets', options);
    if(res.status !== 200){
        alert('Could not fetch the list of networks!');
        return;
    }
    var json = await res.json()
    alert(json)
}
