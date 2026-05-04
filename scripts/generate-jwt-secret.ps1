param(
    [ValidateRange(32, 128)]
    [int]$Bytes = 64
)

$randomBytes = New-Object byte[] $Bytes
$randomGenerator = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$randomGenerator.GetBytes($randomBytes)
$randomGenerator.Dispose()

$secret = [Convert]::ToBase64String($randomBytes)
Write-Output $secret