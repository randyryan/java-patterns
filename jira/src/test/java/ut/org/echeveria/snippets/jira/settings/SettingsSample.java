/*
 * The MIT License
 *
 * Copyright (c) 2019 Li Wan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ut.org.echeveria.snippets.jira.settings;

import org.echeveria.snippets.jira.settings.Settings;

import com.google.common.base.Strings;

class SettingsSample {

  static CrassulaceaeBuilder<Echeveria> echeveria() {
    return Echeveria.newEcheveria();
  }

  static Echeveria echeveria_colorata() {
    return Echeveria.newEcheveria()
        .species("colorata")
        .author("E.Walther")
        .build();
  }

  static Echeveria echeveria_derenbergii() {
    return Echeveria.newEcheveria()
        .species("derenbergii")
        .author("J.A.Purpus")
        .build();
  }

  static Echeveria echeveria_elegans() {
    return Echeveria.newEcheveria()
        .species("elegans")
        .author("Rose")
        .build();
  }

  static Echeveria echeveria_hyalina() {
    return Echeveria.newEcheveria()
        .species("hyalina")
        .author("E.Walther")
        .build();
  }

  static CrassulaceaeBuilder<Graptopetalum> graptopetalum() {
    return Graptopetalum.newGraptopetalum();
  }

  static Graptopetalum graptopetalum_amethystinum() {
    return Graptopetalum.newGraptopetalum()
        .species("amethystinum")
        .author("(Rose) E.Walther")
        .build();
  }

  static Graptopetalum graptopetalum_macdougallii() {
    return Graptopetalum.newGraptopetalum()
        .species("macdougallii")
        .author("Alexander")
        .build();
  }

  static CrassulaceaeBuilder<Pachyphytum> pachyphytum() {
    return Pachyphytum.newPachyphytum();
  }

  static Pachyphytum pachyphytum_oviferum() {
    return Pachyphytum.newPachyphytum()
        .species("oviferum")
        .author("Purpus")
        .build();
  }

  static abstract class Plant extends Settings {

    protected String synonym;

    Plant(String settingsKey, int settingsId) {
      super(settingsKey, settingsId);
    }

    void setSynonym(String synonym) {
      this.synonym = synonym;
    }

    public boolean hasSynonym() {
      return !Strings.isNullOrEmpty(synonym);
    }

    public String getSynonym() {
      return synonym;
    }

  }

  static abstract class Crassulaceae extends Plant {

    protected String genus;
    protected String species;
    protected String author;

    Crassulaceae(String settingsKey, int settingsId) {
      super(settingsKey, settingsId);
    }

    public String getGenus() {
      return genus;
    }

    public String getSpecies() {
      return species;
    }

    public String getAuthor() {
      return author;
    }

    @Override
    public String toString() {
      return genus + " " + species + " " + author;
    }

  }

  static class CrassulaceaeBuilder<C extends Crassulaceae> {

    static <C extends Crassulaceae> CrassulaceaeBuilder<C> get(C crassulaceae) {
      return new CrassulaceaeBuilder<C>(crassulaceae);
    }

    private C crassulaceae;
    private String genus;
    private String species;
    private String author;

    CrassulaceaeBuilder(C crassulaceae) {
      this.crassulaceae = crassulaceae;
    }

    CrassulaceaeBuilder<C> genus(String genus) {
      this.genus = genus;
      return this;
    }

    CrassulaceaeBuilder<C> species(String species) {
      this.species = species;
      return this;
    }

    CrassulaceaeBuilder<C> author(String author) {
      this.author = author;
      return this;
    }

    C build() {
      crassulaceae.genus = this.genus;
      crassulaceae.species = this.species;
      crassulaceae.author = this.author;
      return crassulaceae;
    }

  }

  static class Echeveria extends Crassulaceae {

    static CrassulaceaeBuilder<Echeveria> newEcheveria() {
      return CrassulaceaeBuilder.get(new Echeveria(-1))
          .genus("Echeveria");
    }

    Echeveria(int settingsId) {
      super("echeveria", settingsId);
    }

  }

  static class Graptopetalum extends Crassulaceae {

    static CrassulaceaeBuilder<Graptopetalum> newGraptopetalum() {
      return CrassulaceaeBuilder.get(new Graptopetalum(-1))
          .genus("Graptopetalum");
    }

    Graptopetalum(int settingsId) {
      super("graptopetalum", settingsId);
    }

  }

  static class Pachyphytum extends Crassulaceae {

    static CrassulaceaeBuilder<Pachyphytum> newPachyphytum() {
      return CrassulaceaeBuilder.get(new Pachyphytum(-1))
          .genus("Pachyphytum");
    }

    Pachyphytum(int settingsId) {
      super("pachyphytum", settingsId);
    }

  }

}
