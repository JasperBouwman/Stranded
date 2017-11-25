package com.besaba.revonline.pastebinapi.impl.paste;

import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.paste.internal.Pastes;
import com.besaba.revonline.pastebinapi.response.Response;
import com.besaba.revonline.pastebinapi.response.Responses;



public class PasteImpl implements Paste {

  private final String key;

  private final String title;
  private final long size;

  private final String userFriendlyLanguage;

  private final String machineFriendlyLanguage;
  private final int hits;

  private final PasteVisiblity visiblity;

  private final PasteExpire expire;
  private String raw;
  private final long remainingTime;
  private final long publishDate;

  PasteImpl(final String key,
             final String title,
            final long size,
           final String userFriendlyLanguage,
           final String machineFriendlyLanguage,
            final int hits,
             final PasteVisiblity visiblity,
           final PasteExpire expire,
            final String raw,
            final long publishDate,
            final long remainingTime) {
    this.key = key;
    this.title = title;
    this.size = size;
    this.userFriendlyLanguage = userFriendlyLanguage;
    this.machineFriendlyLanguage = machineFriendlyLanguage;
    this.hits = hits;
    this.visiblity = visiblity;
    this.expire = expire;
    this.raw = raw;
    this.publishDate = publishDate;
    this.remainingTime = remainingTime;
  }


  @Override
  public String getKey() {
    return key;
  }


  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public long getSize() {
    return size;
  }


  @Override
  public String getUserFriendlyLanguage() {
    return userFriendlyLanguage;
  }


  @Override
  public String getMachineFriendlyLanguage() {
    return machineFriendlyLanguage;
  }

  @Override
  public int getHits() {
    return hits;
  }


  @Override
  public PasteVisiblity getVisiblity() {
    return visiblity;
  }


  @Override
  public PasteExpire getExpire() {
    return expire;
  }

  @Override
  public long getPublishDate() {
    return publishDate;
  }

  @Override
  public long getRemainingTime() {
    return remainingTime;
  }


  @Override
  public Response<String> getRaw() {
    // if raw != null it means that I already downloaded the paste or the paste has been builded manually
    if (raw != null) {
      return Responses.success(raw);
    }

    // without a key I cannot download the paste
    if (key == null) {
      return Responses.failed("This paste doesn't support the raw response");
    }

    final Response<String> pasteDownloadResponse = Pastes.download(key);

    if (pasteDownloadResponse.hasError()) {
      return pasteDownloadResponse;
    }

    raw = pasteDownloadResponse.get();
    return Responses.success(raw);
  }
}
